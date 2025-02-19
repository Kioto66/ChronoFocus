import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'course',
        component: () => import('../views/Courses.vue')
    },
    {
        path: '/quiz',
        name: 'Тесты',
        component: () => import('../views/QuestionView.vue')
    },
    {
        path: '/login',
        name: 'login',
        component: () => import( '../views/LoginView.vue')
    },
    //***************************************************************************
    {
        path: '/tasks/AuthForm',
        name: 'AuthForm',
        component: () => import( '../views/Tasks/AuthForm.vue')
    },
    {
        path: '/tasks/Chat',
        name: 'Chat',
        component: () => import( '../views/Tasks/Chat.vue')
    },
    {
        path: '/tasks/FilterProd',
        name: 'FilterProd',
        component: () => import( '../views/Tasks/FilterProd.vue')
    },
    {
        path: '/tasks/Galary',
        name: 'login',
        component: () => import( '../views/Tasks/Galary.vue')
    },
    {
        path: '/tasks/Tasks',
        name: 'Tasks',
        component: () => import( '../views/Tasks/Tasks.vue')
    },
    {
        path: '/tasks/UserList',
        name: 'UserList',
        component: () => import( '../views/Tasks/UserList.vue')
    },
    {
        path: '/tasks/Timer',
        name: 'Timer',
        component: () => import( '../views/Tasks/Timer.vue')
    },
    {
        path: '/logout',
        name: 'logout',
        component: () => import( '../views/LoginView.vue')
    }
]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

export default router