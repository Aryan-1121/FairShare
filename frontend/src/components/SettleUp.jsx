import React, { useState } from 'react';
import { createTransaction } from '../services/apiService';

const SettleUp = () => {
  const [payerId, setPayerId] = useState('');
  const [payeeId, setPayeeId] = useState('');
  const [amount, setAmount] = useState('');
  const [expenseId, setExpenseId] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    const transaction = { payer: { id: payerId }, payee: { id: payeeId }, amount, expense: { id: expenseId } };
    try {
      await createTransaction(transaction);
      alert('Transaction created successfully');
    } catch (error) {
      console.error('Error creating transaction:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input type="text" placeholder="Payer ID" value={payerId} onChange={(e) => setPayerId(e.target.value)} />
      <input type="text" placeholder="Payee ID" value={payeeId} onChange={(e) => setPayeeId(e.target.value)} />
      <input type="number" placeholder="Amount" value={amount} onChange={(e) => setAmount(e.target.value)} />
      <input type="text" placeholder="Expense ID" value={expenseId} onChange={(e) => setExpenseId(e.target.value)} />
      <button type="submit">Settle Up</button>
    </form>
  );
};

export default SettleUp;
