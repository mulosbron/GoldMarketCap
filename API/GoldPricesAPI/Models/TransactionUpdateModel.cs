namespace GoldMarketCapAPI.Models
{
    public class TransactionUpdateModel
    {
        public DateTime? Date { get; set; }
        public string TransactionType { get; set; }
        public decimal? Amount { get; set; }
        public decimal? Price { get; set; }
    }

}
