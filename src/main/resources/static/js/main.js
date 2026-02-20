// 1. Select a stock from the left-side watchlist
function selectStock(symbol, price) {
    document.getElementById('currentStockSymbol').value = symbol;
    document.getElementById('currentStockPrice').value = price;
    document.getElementById('selectedStockLabel').innerText = "Trading: " + symbol + " @ ₹" + price;
    updateTotalValue();
}

// 2. Automatically calculate total value when user types a quantity
document.getElementById('tradeQuantity').addEventListener('input', updateTotalValue);

function updateTotalValue() {
    let qty = document.getElementById('tradeQuantity').value;
    let price = document.getElementById('currentStockPrice').value;
    
    if(qty && price) {
        let total = (qty * price).toFixed(2);
        document.getElementById('totalValueDisplay').value = "Total Value: ₹" + total;
    } else {
        document.getElementById('totalValueDisplay').value = "Total Value: ₹0.00";
    }
}

// 3. Send the Buy/Sell order to the Spring Boot Backend
async function executeTrade(type) {
    let symbol = document.getElementById('currentStockSymbol').value;
    let price = document.getElementById('currentStockPrice').value;
    let qty = document.getElementById('tradeQuantity').value;
    let userId = document.getElementById('userId').value;
    let messageDiv = document.getElementById('tradeMessage');

    // Basic form validation
    if(!symbol || !qty || qty <= 0) {
        messageDiv.style.color = "#f44336"; // Red
        messageDiv.innerText = "Please select a stock and enter a valid quantity.";
        return;
    }

    // Build the JSON payload matching your TradeRequest.java DTO
    let payload = {
        userId: userId,
        stockSymbol: symbol,
        quantity: parseInt(qty),
        tradeType: type,
        currentPrice: parseFloat(price)
    };

    messageDiv.style.color = "#ff9800"; // Orange
    messageDiv.innerText = "Sending order to exchange...";

    try {
        let response = await fetch('/api/trade/execute', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        let resultText = await response.text();

        if(response.ok) {
            messageDiv.style.color = "#4caf50"; // Green
            messageDiv.innerText = "✅ " + resultText;
            
            // Reload the page after a brief moment to update the Virtual Cash balance
            setTimeout(() => window.location.reload(), 1500);
        } else {
            // Displays the specific backend error (e.g., "Insufficient funds!")
            messageDiv.style.color = "#f44336"; 
            messageDiv.innerText = "❌ Order Failed: " + resultText;
        }
    } catch(error) {
        messageDiv.style.color = "#f44336";
        messageDiv.innerText = "❌ Network error. Check server console.";
    }
}

