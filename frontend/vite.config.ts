// vite.config.ts
/// <reference types="vitest" />
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  test: {
    globals: true,
    environment: 'jsdom',
    setupFiles: './src/setupTests.ts',
    // Adicione esta seção para mocar os imports de CSS
    css: true,
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'], // Formatos do relatório
      reportsDirectory: './tests/coverage', // Onde salvar os relatórios
      all: true,
      include: ['src/**/*.ts', 'src/**/*.tsx'],
      exclude: [
        'src/main.tsx',      // Excluir o ponto de entrada da aplicação
        'src/vite-env.d.ts', // Excluir definições de tipo do Vite
        'src/types',         // Excluir ficheiros que contêm apenas tipos
        // Adicione aqui outros ficheiros/pastas a serem ignorados
      ],
      thresholds: {
        lines: 60,
        functions: 50, // Podemos ter um limiar diferente para funções
        branches: 60,
        statements: 60,
      }
    },
  },
  // Adicione esta seção para mocar os imports de imagens
  resolve: {
    alias: {
      '\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$':
        new URL('./tests/fileMock.ts', import.meta.url).pathname,
    },
  },
});
