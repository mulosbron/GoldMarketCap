﻿using MongoDB.Driver;
using GoldMarketCapAPI.Models;

public class MongoDBContext
{
    private readonly IMongoDatabase _database;

    public MongoDBContext(string connectionString, string databaseName)
    {
        var client = new MongoClient(connectionString);
        _database = client.GetDatabase(databaseName);
    }

    public IMongoCollection<GoldPrice> Prices
    {
        get
        {
            return _database.GetCollection<GoldPrice>("prices");
        }
    }

    public IMongoCollection<DailyPercentage> DailyPercentages
    {
        get
        {
            return _database.GetCollection<DailyPercentage>("daily_percentages");
        }
    }

    public IMongoCollection<User> Users
    {
        get
        {
            return _database.GetCollection<User>("users");
        }
    }

    public IMongoCollection<Portfolio> Portfolios
    {
        get
        {
            return _database.GetCollection<Portfolio>("portfolios");
        }
    }
}

