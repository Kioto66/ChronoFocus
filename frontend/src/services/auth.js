import { ref } from 'vue';

// Реф-переменная для хранения логина и пароля
const refAuth = ref({ login: '', password: '' });

// Функция для авторизации
const login = async () => {
    try {
        const response = await fetch('https://k4nk.ru/bot-api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                login: refAuth.value.login,
                password: refAuth.value.password,
            }),
        });

        if (!response.ok) {
            throw new Error('Ошибка авторизации');
        }

        const data = await response.json();
        console.log('Ответ сервера:', data);

        const { accessToken, refreshToken, type } = data;
        console.log('Access Token:', accessToken);
        console.log('Refresh Token:', refreshToken);
        console.log('Token Type:', type);

        if (accessToken) {
            const tokenParts = accessToken.split('.');
            if (tokenParts.length === 3) {
                const [header, payload, signature] = tokenParts;
                console.log('Header:', JSON.parse(atob(header)));
                console.log('Payload:', JSON.parse(atob(payload)));
                console.log('Signature:', signature);
            } else {
                console.error('Некорректный формат accessToken');
            }
        }
        saveTokens (accessToken, refreshToken);
        console.log('Wright Refresh Token:', refreshToken);
        return { accessToken, refreshToken, type };
    } catch (error) {
        console.error('Ошибка:', error.message);
        throw error;
    }
};

// Функция для обновления токена
const refreshT = async () => {
    try {
        // Получаем refreshToken из localStorage
        const curRefreshToken = localStorage.getItem('refreshToken');
        if (!curRefreshToken) { // Исправлено: проверяем curRefreshToken
            throw new Error('Refresh token не найден');
        }

        // Отправляем POST-запрос на сервер
        const response = await fetch('https://k4nk.ru/bot-api/auth/refresh', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ refreshToken: curRefreshToken }), // Исправлено: передаем curRefreshToken
        });

        // Если ответ не успешный, выбрасываем ошибку
        if (!response.ok) {
            const errorData = await response.json(); // Пытаемся получить JSON с ошибкой
            throw new Error(errorData.message || 'Ошибка обновления токена');
        }

        // Получаем данные ответа
        const data = await response.json();
        console.log('Ответ сервера:', data);

        // Извлекаем новый accessToken и refreshToken
        const { accessToken, refreshToken, type } = data;

        // Сохраняем новые токены в localStorage
        saveTokens(accessToken, refreshToken);

        console.log('Токены обновлены!');
        console.log('Новый Access Token:', accessToken);
        console.log('Новый Refresh Token:', refreshToken);

        return { accessToken, refreshToken, type }; // Возвращаем объект
    } catch (error) {
        console.error('Ошибка:', error.message);
        throw error; // Пробрасываем ошибку для обработки в компоненте
    }
};

// Сохранение токенов
const saveTokens = (accessToken, refreshToken) => {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
};

// Функция для проверки срока действия токена
const isTokenExpired = (token) => {
    if (!token) return true;

    const payload = JSON.parse(atob(token.split('.')[1]));
    const expirationTime = payload.exp * 1000; // Время истечения в миллисекундах
    return Date.now() > expirationTime;
};

// Функция для получения актуального accessToken
const getAccessToken = async () => {
    let accessToken = localStorage.getItem('accessToken');

    // Если токен истёк, обновляем его
    if (isTokenExpired(accessToken)) {
        try {
            const { accessToken: newAccessToken } = await refreshT(); // Используем refreshT
            accessToken = newAccessToken;
        } catch (error) {
            console.error('Ошибка при обновлении токена:', error.message);
            throw error;
        }
    }

    return accessToken;
};

// Получение токенов
const getTokens = () => {
    return {
        accessToken: localStorage.getItem('accessToken'),
        refreshToken: localStorage.getItem('refreshToken'),
    };
};

// Удаление токенов (для выхода)
const clearTokens = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
};

// Проверка авторизации
const isAuthenticated = () => {
    const accessToken = localStorage.getItem('accessToken');
    return !!accessToken;
};

export { refAuth, login, getAccessToken, refreshT, saveTokens, getTokens, clearTokens, isAuthenticated };