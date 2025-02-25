import axios from "axios";

// Базовый URL сервера
const API_BASE_URL = "http://192.168.1.112/bot-api";

// Создаем экземпляр Axios с базовой конфигурацией
const apiClient = axios.create({
    baseURL: API_BASE_URL,
    timeout: 10000, // Таймаут запроса
    headers: {
        "Content-Type": "application/json", // Указываем, что отправляем JSON
    },
});

// Все методы API
const ApiService = {
    // Категории
    getCategories: async () => {
        try {
            const response = await apiClient.get("/category");
            return response.data.payload;
        } catch (error) {
            console.error("Ошибка при получении категорий:", error);
            throw error;
        }
    },

    createCategory: async (categoryData) => {
        try {
            const response = await apiClient.post("/category", {
                apiVer: "1",
                payload: [categoryData],
                status: "OK",
            });
            return response.data;
        } catch (error) {
            console.error("Ошибка при создании категории:", error);
            throw error;
        }
    },

    // Таймер
    getCurrentTracker: async () => {
        try {
            const response = await apiClient.get("/current_tracker");
            return response.data.payload;
        } catch (error) {
            console.error("Ошибка при получении текущего таймера:", error);
            throw error;
        }
    },

    startTracker: async (id) => {
        try {
            const response = await apiClient.get("/start",{
                params: {
                    category_id: id,
                },
            });
            return response.data;
        } catch (error) {
            console.error("Ошибка при запуске таймера:", error);
            throw error;
        }
    },

    stopTracker: async (id) => {
        try {
            const response = await apiClient.get("/stop",{
                params: {
                    category_id: id,
                },
            });
            return response.data;
        } catch (error) {
            console.error("Ошибка при остановке таймера:", error);
            throw error;
        }
    },

    // Статистика
    getSummary: async (fromDate, toDate) => {
        try {
            const response = await apiClient.get("/summary", {
                params: {
                    from: fromDate,
                    to: toDate,
                },
            });
            return response.data.payload;
        } catch (error) {
            console.error("Ошибка при получении сводки:", error);
            throw error;
        }
    },

    // Документация
    getHelp: async () => {
        try {
            const response = await apiClient.get("/help");
            return response.data;
        } catch (error) {
            console.error("Ошибка при загрузке документации:", error);
            throw error;
        }
    },
};

// Экспортируем сервис
export default ApiService;