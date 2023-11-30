CREATE TABLE Inventory (
    ItemID INT AUTO_INCREMENT,
    ItemName VARCHAR(255),
    Quantity INT,
    Price DECIMAL(10,2),
    SupplierID INT,
    DateAdded TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ExpiryDate DATE,
    ProductDetails TEXT,
    QuantitySold INT,
    PRIMARY KEY(ItemID)
);

INSERT INTO Inventory (ItemName, Quantity, Price, SupplierID, ExpiryDate, ProductDetails)
VALUES 
('Chicken Thigh', 100, 2.50, 1, '2023-12-31', 'Fresh chicken thighs sourced from local farms.'),
('Cookies', 200, 1.00, 2, '2024-01-31', 'Delicious chocolate chip cookies baked daily.'),
('Apple', 150, 0.50, 3, '2023-11-30', 'Crisp and sweet apples from organic orchards.'),
('Avocado', 120, 1.20, 4, '2023-11-15', 'Creamy avocados perfect for salads and guacamole.'),
('Milk', 80, 3.00, 5, '2023-11-10', 'Fresh whole milk from local dairy farms.');

select * from inventory;