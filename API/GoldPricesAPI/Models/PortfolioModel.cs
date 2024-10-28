using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;

namespace GoldMarketCapAPI.Models
{
    public class Portfolio
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }

        [BsonElement("Username")]
        public required string Username { get; set; }

        [BsonElement("GoldTypes")]
        public List<GoldType> GoldTypes { get; set; } = new List<GoldType>();
    }

    public class GoldType
    {
        [BsonElement("Type")]
        public required string Type { get; set; }

        [BsonElement("Transactions")]
        public List<Transaction> Transactions { get; set; } = new List<Transaction>();
    }

    public class Transaction
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }

        [BsonElement("Date")]
        public DateTime Date { get; set; }

        [BsonElement("TransactionType")]
        public required string TransactionType { get; set; }

        [BsonElement("Amount")]
        public decimal Amount { get; set; }

        [BsonElement("Price")]
        public decimal Price { get; set; }
    }
}
