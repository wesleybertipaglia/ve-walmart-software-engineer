import pandas as pd
import sqlite3

data_0 = pd.read_csv('./data/shipping_data_0.csv')
data_1 = pd.read_csv('./data/shipping_data_1.csv')
data_2 = pd.read_csv('./data/shipping_data_2.csv')

conn = sqlite3.connect('shipment_database.db')
cursor = conn.cursor()

def get_product_id(product_name):
    cursor.execute('SELECT id FROM product WHERE name = ?', (product_name,))
    result = cursor.fetchone()
    if result:
        return result[0]
    cursor.execute('INSERT INTO product (name) VALUES (?)', (product_name,))
    return cursor.lastrowid

for index, row in data_0.iterrows():
    product_id = get_product_id(row['product'])
    cursor.execute('''
    INSERT INTO shipment (product_id, quantity, origin, destination)
    VALUES (?, ?, ?, ?)
    ''', (product_id, row['product_quantity'], row['origin_warehouse'], row['destination_store']))

combined_data = pd.merge(data_1, data_2, on='shipment_identifier')
grouped_data = combined_data.groupby(['shipment_identifier', 'product', 'origin_warehouse', 'destination_store']).size().reset_index(name='quantity')

for index, row in grouped_data.iterrows():
    product_id = get_product_id(row['product'])
    cursor.execute('''
    INSERT INTO shipment (product_id, quantity, origin, destination)
    VALUES (?, ?, ?, ?)
    ''', (product_id, row['quantity'], row['origin_warehouse'], row['destination_store']))

conn.commit()
conn.close()
