// src/App.test.tsx
import React from 'react'; 
import { render, screen } from '@testing-library/react';
import { describe, it, expect } from 'vitest';
import '@testing-library/jest-dom';
import App from './App';

describe('App', () => {
  it('renders the main App component', () => {
    render(<App />);
    // Procure por um texto ou elemento que você sabe que existe em App.tsx
    // Este é apenas um exemplo, ajuste conforme necessário.
    expect(screen.getByText(/vite \+ react/i)).toBeInTheDocument();
  });
});