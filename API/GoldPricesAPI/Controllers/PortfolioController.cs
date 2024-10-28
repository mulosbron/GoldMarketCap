using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using GoldMarketCapAPI.Models;
using System.Collections.Generic;
using System.Threading.Tasks;
using MongoDB.Bson;

namespace GoldMarketCapAPI.Controllers
{
    [ApiController]
    [Route("api/portfolio")]
    public class PortfolioController(MongoDBContext context) : ControllerBase
    {
        private readonly IMongoCollection<Portfolio> _portfolios = context.Portfolios;

        [HttpGet("transactions")]
        public async Task<IActionResult> GetTransactionsByUsername(string username)
        {
            var portfolio = await _portfolios.Find(p => p.Username == username).FirstOrDefaultAsync();
            if (portfolio == null)
            {
                return NotFound(new { Message = "No portfolio found."});
            }

            var transactionsDict = new Dictionary<string, List<Transaction>>();

            foreach (var goldType in portfolio.GoldTypes)
            {
                transactionsDict.Add(goldType.Type, goldType.Transactions);
            }

            return Ok(transactionsDict);
        }

        [HttpPost("add-transaction")]
        public async Task<IActionResult> AddTransaction(string username, string goldType, [FromBody] Transaction transaction)
        {
            if (transaction == null)
            {
                return BadRequest(new { Message =  "Transaction data is missing." });
            }

            var portfolio = await _portfolios.Find(p => p.Username == username).FirstOrDefaultAsync();
            if (portfolio == null)
            {
                portfolio = new Portfolio
                {
                    Username = username,
                    GoldTypes = new List<GoldType>()
                };
                await _portfolios.InsertOneAsync(portfolio);
            }

            var goldTypeObj = portfolio.GoldTypes.FirstOrDefault(gt => gt.Type == goldType);
            if (goldTypeObj == null)
            {
                goldTypeObj = new GoldType
                {
                    Type = goldType,
                    Transactions = new List<Transaction>()
                };
                portfolio.GoldTypes.Add(goldTypeObj);
            }

            transaction.Id = ObjectId.GenerateNewId().ToString();
            goldTypeObj.Transactions.Add(transaction);

            await _portfolios.ReplaceOneAsync(p => p.Id == portfolio.Id, portfolio);

            return Ok(new { Message = "Transaction added successfully." });
        }

        [HttpGet("get-transaction")]
        public async Task<IActionResult> GetTransaction(string username, string transactionId)
        {
            var portfolio = await _portfolios.Find(p => p.Username == username).FirstOrDefaultAsync();
            if (portfolio == null)
            {
                return NotFound(new { Message = "No portfolio found."});
            }

            Transaction? transactionToView = null;
            foreach (var goldType in portfolio.GoldTypes)
            {
                transactionToView = goldType.Transactions.FirstOrDefault(t => t.Id == transactionId);
                if (transactionToView != null)
                {
                    break;
                }
            }

            if (transactionToView == null)
            {
                return NotFound(new { Message = "Transaction not found." });
            }

            return Ok(transactionToView);
        }

        [HttpPatch("update-transaction")]
        public async Task<IActionResult> UpdateTransaction(string username, string transactionId, [FromBody] TransactionUpdateModel updateModel)
        {
            var portfolio = await _portfolios.Find(p => p.Username == username).FirstOrDefaultAsync();
            if (portfolio == null)
            {
                return NotFound(new { Message = "No portfolio found." });
            }

            bool transactionFound = false;
            foreach (var goldType in portfolio.GoldTypes)
            {
                var transactionToUpdate = goldType.Transactions.FirstOrDefault(t => t.Id == transactionId);
                if (transactionToUpdate != null)
                {
                    if (updateModel.Date != null)
                        transactionToUpdate.Date = updateModel.Date.Value;
                    if (!string.IsNullOrWhiteSpace(updateModel.TransactionType))
                        transactionToUpdate.TransactionType = updateModel.TransactionType;
                    if (updateModel.Amount != null)
                        transactionToUpdate.Amount = updateModel.Amount.Value;
                    if (updateModel.Price != null)
                        transactionToUpdate.Price = updateModel.Price.Value;

                    transactionFound = true;
                    break;
                }
            }

            if (!transactionFound)
            {
                return NotFound(new { Message = "Transaction not found." });
            }

            await _portfolios.ReplaceOneAsync(p => p.Id == portfolio.Id, portfolio);

            return Ok(new { Message =  "Transaction updated successfully." });
        }

        [HttpDelete("delete-goldtype")]
        public async Task<IActionResult> DeleteGoldType(string username, string goldType)
        {
            var portfolio = await _portfolios.Find(p => p.Username == username).FirstOrDefaultAsync();
            if (portfolio == null)
            {
                return NotFound(new { Message = "No portfolio found." });
            }

            var goldTypeExists = portfolio.GoldTypes.Any(gt => gt.Type == goldType);
            if (!goldTypeExists)
            {
                return NotFound(new { Message = "GoldType not found in portfolio." });
            }

            portfolio.GoldTypes.RemoveAll(gt => gt.Type == goldType);

            await _portfolios.ReplaceOneAsync(p => p.Id == portfolio.Id, portfolio);

            return Ok(new { Message = "GoldType and its transactions have been deleted successfully." });
        }

        [HttpDelete("delete-transaction")]
        public async Task<IActionResult> DeleteTransaction(string username, string goldType, string transactionId)
        {
            var portfolio = await _portfolios.Find(p => p.Username == username).FirstOrDefaultAsync();
            if (portfolio == null)
            {
                return NotFound(new { Message = "No portfolio found." });
            }

            var goldTypeObj = portfolio.GoldTypes.FirstOrDefault(gt => gt.Type == goldType);
            if (goldTypeObj == null)
            {
                return NotFound(new { Message = "GoldType not found in portfolio." });
            }

            var transactionExists = goldTypeObj.Transactions.Any(t => t.Id == transactionId);
            if (!transactionExists)
            {
                return NotFound(new { Message = "Transaction not found." });
            }

            goldTypeObj.Transactions.RemoveAll(t => t.Id == transactionId);

            await _portfolios.ReplaceOneAsync(p => p.Id == portfolio.Id, portfolio);

            return Ok(new { Message = "Transaction has been deleted successfully." });
        }

    }
}
