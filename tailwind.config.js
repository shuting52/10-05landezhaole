/** @type {import('tailwindcss').Config} */
export default {
  darkMode: ["class"],
  content: ["./index.html", "./src/**/*.{ts,tsx}"],
  theme: {
    container: {
      center: true,
      padding: "1.5rem",
      screens: { "2xl": "1400px" },
    },
    extend: {
      colors: {
        // 现代国潮色板
        paper: "#F7F3EA", // 宣纸米白
        "paper-soft": "#FBF9F4", // 更浅的暖白
        ink: "#193B3D", // 深黛青
        "ink-soft": "#245052", // 深黛青浅一档
        cinnabar: "#C63C32", // 朱砂红
        "cinnabar-light": "#E76F61", // 辰砂浅红
        gold: "#C99A3D", // 鎏金
        "gold-light": "#E4C77E", // 浅金
        inkblack: "#202322", // 墨黑
        mist: "#E8E5DE", // 辅助浅灰
        "mist-soft": "#F1EEE7",
        border: "#E8E5DE",
        ring: "#C63C32",
        background: "#F7F3EA",
        foreground: "#202322",
      },
      borderRadius: {
        xl: "0.875rem",
        "2xl": "1rem",
        "3xl": "1.25rem",
      },
      boxShadow: {
        soft: "0 1px 2px rgba(32,35,34,0.04), 0 4px 16px rgba(25,59,61,0.06)",
        card: "0 1px 3px rgba(32,35,34,0.05), 0 8px 24px rgba(25,59,61,0.07)",
        seal: "0 2px 8px rgba(198,60,50,0.28)",
      },
      fontFamily: {
        sans: [
          '"PingFang SC"',
          '"Hiragino Sans GB"',
          '"Microsoft YaHei"',
          '"Noto Sans SC"',
          "system-ui",
          "-apple-system",
          "sans-serif",
        ],
        serif: ['"Songti SC"', '"Noto Serif SC"', "Georgia", "serif"],
      },
      keyframes: {
        "accordion-down": {
          from: { height: "0" },
          to: { height: "var(--radix-accordion-content-height)" },
        },
        "accordion-up": {
          from: { height: "var(--radix-accordion-content-height)" },
          to: { height: "0" },
        },
        "fade-in": {
          from: { opacity: "0", transform: "translateY(4px)" },
          to: { opacity: "1", transform: "translateY(0)" },
        },
      },
      animation: {
        "accordion-down": "accordion-down 0.2s ease-out",
        "accordion-up": "accordion-up 0.2s ease-out",
        "fade-in": "fade-in 0.25s ease-out",
      },
    },
  },
  plugins: [require("tailwindcss-animate")],
};
