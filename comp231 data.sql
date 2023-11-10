-- CREATE TABLE Inventory (
--     ItemID INT AUTO_INCREMENT,
--     ItemName VARCHAR(255),
--     Quantity INT,
--     Price DECIMAL(10,2),
--     SupplierID INT,
--     DateAdded TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     ExpiryDate DATE,
--     ProductDetails TEXT,
--     PRIMARY KEY(ItemID)
-- );

-- INSERT INTO Inventory (ItemName, Quantity, Price, SupplierID, ExpiryDate, ProductDetails)
-- VALUES 
-- ('Chicken Thigh', 100, 2.50, 1, '2023-12-31', 'Fresh chicken thighs sourced from local farms.'),
-- ('Cookies', 200, 1.00, 2, '2024-01-31', 'Delicious chocolate chip cookies baked daily.'),
-- ('Apple', 150, 0.50, 3, '2023-11-30', 'Crisp and sweet apples from organic orchards.'),
-- ('Avocado', 120, 1.20, 4, '2023-11-15', 'Creamy avocados perfect for salads and guacamole.'),
-- ('Milk', 80, 3.00, 5, '2023-11-10', 'Fresh whole milk from local dairy farms.');

-- select * from inventory;

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




