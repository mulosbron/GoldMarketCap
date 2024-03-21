import numpy as np
import pandas as pd
from pymongo import MongoClient
from datetime import datetime

# Global Constants
CONNECTION_STRING = "mongodb://localhost:27017"
DB_NAME = 'gold_prices'
COLLECTION_NAME = 'prices'
PERCENTAGE_DIFF_COLLECTION = 'daily_percentage'
BASE_HEX = "15180"
DATE_FORMAT = '%Y-%m-%d %H:%M'
DATE_FIELD = 'date'


def custom_print(*args, **kwargs):
    current_time = datetime.now().strftime(DATE_FORMAT)
    print(f"[{current_time}]", *args, **kwargs)


'''
def find_specified_and_related_documents():
    custom_print("Searching for specified and related documents...")
    date = "[0x65f1a350]Milliyet"
    with MongoClient(CONNECTION_STRING) as client:
        collection = client[DB_NAME][COLLECTION_NAME]
        specified_document = collection.find_one({'date': f'{date}'})

        if not specified_document or 'date' not in specified_document:
            custom_print("Document with the specified hex value not found or does not contain 'date'.")
            return None, None

        hex_value = specified_document['date'].split("[")[1].split("]")[0]
        result_hex = f"0x{format(int(hex_value, 16) - int(BASE_HEX, 16), 'X')}".lower()

        related_document = collection.find_one({'date': f"[{result_hex}]Milliyet"})

        if related_document:
            custom_print("Related document found successfully.")
        else:
            custom_print("Related document not found.")

        return specified_document, related_document
'''


def find_latest_and_related_documents():
    custom_print("Searching for the latest and related documents...")
    with MongoClient(CONNECTION_STRING) as client:
        collection = client[DB_NAME][COLLECTION_NAME]
        latest_document = collection.find_one(sort=[('_id', -1)])
        if not latest_document or 'date' not in latest_document:
            custom_print("The latest document added was not found or does not contain 'date'.")
            return None, None

        hex_value = latest_document['date'].split("[")[1].split("]")[0]
        result_hex = f"0x{format(int(hex_value, 16) - int(BASE_HEX, 16), 'X')}".lower()
        related_document = collection.find_one({'date': f"[{result_hex}]Milliyet"})
        if related_document:
            custom_print("Related document found successfully.")
        else:
            custom_print("Related document not found.")
        return latest_document, related_document


def calculate_percentage_difference(df1, df2):
    custom_print("Calculating percentage differences between DataFrames...")

    def percentage_diff(x1, x2):
        return np.where((x1 != 0) & ~np.isnan(x1) & ~np.isnan(x2), (x2 - x1) / x1 * 100, np.nan)

    return df1.combine(df2, func=percentage_diff)


def save_percentage_difference_to_mongodb(json1, percentage_diff):
    custom_print("Saving percentage differences to MongoDB...")
    with MongoClient(CONNECTION_STRING) as client:
        db = client[DB_NAME]
        collection = db[PERCENTAGE_DIFF_COLLECTION]
        document = {
            DATE_FIELD: json1[DATE_FIELD],
            "percentage_difference": percentage_diff.to_dict()
        }
        collection.insert_one(document)
        custom_print("Percentage differences successfully saved.")


def process_and_clean_data():
    custom_print("Starting data cleaning and comparison process...")
    json1, json2 = find_latest_and_related_documents()
    if not json1 or not json2:
        custom_print("Required documents not found.")
        return

    data1 = pd.DataFrame(json1['data'])
    data2 = pd.DataFrame(json2['data'])
    custom_print("Data cleaned and converted to DataFrames.")

    percentage_diff = calculate_percentage_difference(data1, data2)
    custom_print("Percentage Differences:")
    custom_print(percentage_diff.values)

    save_percentage_difference_to_mongodb(json1, percentage_diff)


if __name__ == "__main__":
    process_and_clean_data()
