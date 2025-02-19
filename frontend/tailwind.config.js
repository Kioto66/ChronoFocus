/** @type {import('tailwindcss').Config} */
import primeUi from 'tailwindcss-primeui';

export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors(theme) {
        return {
          red: {
            ...theme.colors.red,
            300: 'hsl(15, 100%, 70%)',
            200: 'hsl(348, 100%, 68%)'
          },
          green: {
            ...theme.colors.green,
            300: 'hsl(145, 58%, 55%)'
          },
          violet: {
            ...theme.colors.violet,
            300: 'hsl(264, 64%, 52%)'
          },
          orange:{
            ...theme.colors.orange,
            300: 'hsl(43, 84%, 65%)'
          },
          blue: {
            ...theme.colors.blue,
            100: 'hsl(195, 74%, 62%)',
            200: 'hsl(236, 100%, 87%)',
            300: 'hsl(235, 45%, 61%)',
            600: 'hsl(235, 46%, 20%)',
            900: 'hsl(226, 43%, 10%)'
          },
          primary: {
            DEFAULT: 'hsl(246, 80%, 60%)',

          }
        }
      }
    },
   },
  plugins: [primeUi]
}

