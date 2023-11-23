

CREATE TABLE Inventory (
    ItemID INT AUTO_INCREMENT,
    ItemName VARCHAR(255),
    Quantity INT,
    OptimumLevel INT DEFAULT 500,  -- Add the new column with a default value
    Price DECIMAL(10,2),
    SupplierID INT,
    DateAdded TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ExpiryDate DATE,
    ProductDetails TEXT,
    PRIMARY KEY(ItemID)
);

CREATE TABLE `Order` (
    OrderID INT AUTO_INCREMENT,
    ItemID INT,
    Quantity INT,
    OrderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- Add other columns from Inventory table
    ItemName VARCHAR(255),
    OptimumLevel INT DEFAULT 500, -- Set the default value for OptimumLevel
    Price DECIMAL(10,2),
    SupplierID INT,
    ExpiryDate DATE,
    ProductDetails TEXT,
    PRIMARY KEY(OrderID),
    FOREIGN KEY(ItemID) REFERENCES Inventory(ItemID) -- Assuming you have an Inventory table
);


CREATE TABLE Disposal (
    DisposalID INT AUTO_INCREMENT,
    ItemID INT,
    ItemName VARCHAR(255), -- Add ItemName column
    QuantityDisposed INT,
    DisposalDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DisposalReason TEXT,
    PRIMARY KEY(DisposalID),
    FOREIGN KEY(ItemID) REFERENCES Inventory(ItemID)
);



INSERT INTO Inventory (ItemName, Quantity, OptimumLevel, Price, SupplierID, ExpiryDate, ProductDetails)
VALUES 
('Chicken Thigh', 100, 150, 2.50, 1, '2023-12-31', 'Fresh chicken thighs sourced from local farms.'),
('Cookies', 200, 250, 1.00, 2, '2024-01-31', 'Delicious chocolate chip cookies baked daily.'),
('Apple', 150, 200, 0.50, 3, '2023-11-30', 'Crisp and sweet apples from organic orchards.'),
('Avocado', 120, 180, 1.20, 4, '2023-11-15', 'Creamy avocados perfect for salads and guacamole.'),
('Milk', 80, 120, 3.00, 5, '2023-11-10', 'Fresh whole milk from local dairy farms.');



DELIMITER //

-- Trigger to log disposal events
CREATE TRIGGER AfterInventoryUpdate
AFTER UPDATE ON Inventory
FOR EACH ROW
BEGIN
    -- Check if the quantity has decreased (indicating disposal)
    IF NEW.Quantity < OLD.Quantity THEN
        -- Insert a record into the Disposal table
        INSERT INTO Disposal (ItemID, ItemName, QuantityDisposed, DisposalReason)
        VALUES (OLD.ItemID, OLD.ItemName, OLD.Quantity - NEW.Quantity, 'Disposed during update');
    END IF;
END;//


-- Select the updated inventory
SELECT * FROM Inventory;

-- Select disposal records from the Disposal table
SELECT * FROM Disposal;

CREATE TABLE Sales (
    SaleID INT AUTO_INCREMENT,
    ItemID INT,
    ItemName VARCHAR(255),
    QuantitySold INT,
    SaleDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(SaleID),
    FOREIGN KEY(ItemID) REFERENCES Inventory(ItemID)
);

-- Example data for the Sales table
INSERT INTO Sales (ItemID, ItemName, QuantitySold)
VALUES
(1, 'Chicken Thigh', 5),
(2, 'Cookies', 10),
(3, 'Apple', 8),
(4, 'Avocado', 3),
(5, 'Milk', 6);

select * from sales;



