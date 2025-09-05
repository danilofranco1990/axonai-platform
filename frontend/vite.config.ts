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
  },
  // Adicione esta seção para mocar os imports de imagens
  resolve: {
    alias: {
      '\\.(jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga)$':
        new URL('./tests/fileMock.ts', import.meta.url).pathname,
    },
  },
});
