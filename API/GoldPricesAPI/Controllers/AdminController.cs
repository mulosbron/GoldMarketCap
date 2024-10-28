using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Driver;
using GoldMarketCapAPI.Models;

namespace GoldMarketCapAPI.Controllers
{
    [ApiController]
    [Route("api/admin")]
    public class AdminController(MongoDBContext context) : ControllerBase
    {
        private readonly IMongoCollection<User> _users = context.Users;

        [HttpGet("users")]
        [Authorize(Roles = "admin")]
        public async Task<IActionResult> GetAllUsers()
        {
            var users = await _users.Find(user => true).Project(u => new { u.Email, u.Username }).ToListAsync();

            if (users == null || users.Count == 0)
            {
                return NotFound(new { Message = "No users were found." });
            }

            return Ok(new { users });
        }

        [HttpPost("ban/{username}")]
        [Authorize(Roles = "admin")]
        public async Task<IActionResult> BanUserByUsername(string username, int days)
        {
            var filter = Builders<User>.Filter.Eq(u => u.Username, username);
            var update = Builders<User>.Update.Set(u => u.BannedUntil, DateTime.UtcNow.AddDays(days));
            var result = await _users.UpdateOneAsync(filter, update);

            if (result.ModifiedCount == 0)
            {
                return NotFound(new { Message = "User was not found." });
            }

            return Ok(new { Message = $"User '{username}' was successfully banned! Ban duration: {days} days." });
        }
    }
}
