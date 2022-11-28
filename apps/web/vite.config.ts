import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import tsconfigPaths from 'vite-tsconfig-paths';

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    commonjsOptions: {
      include: [/node_modules/],
    },
  },
  plugins: [
    react({
      babel: {
        babelrc: true,
      },
    }),
    tsconfigPaths(),
  ],
});
