import Layout from '../layout/Layout.vue'
import {createRouter, createWebHistory} from "vue-router";

/**
 * 角色路由权限配置
 */
const ROLE_ROUTES = {
    admin: [
        '/stuInfo', '/dormManagerInfo', '/buildingInfo', '/roomInfo',
        '/noticeInfo', '/adjustRoomInfo', '/repairInfo', '/visitorInfo',
        '/selfInfo', '/home'
    ],
    dormManager: [
        '/buildingInfo', '/roomInfo', '/noticeInfo', '/repairInfo',
        '/visitorInfo', '/selfInfo', '/home'
    ],
    student: [
        '/myRoomInfo', '/applyRepairInfo', '/applyChangeRoom',
        '/selfInfo', '/home'
    ]
}

/**
 * 检查用户是否有权限访问指定路径
 * @param {string} identity - 用户角色
 * @param {string} path - 要访问的路径
 * @returns {boolean} - 是否有权限
 */
function hasPermission(identity, path) {
    if (path === '/Login' || path === '/Layout' || path === '/') {
        return true
    }

    const allowedPaths = ROLE_ROUTES[identity]
    if (!allowedPaths) {
        return false
    }

    return allowedPaths.some(allowedPath => path.startsWith(allowedPath))
}

export const constantRoutes = [
    {path: '/Login', name: 'Login', component: () => import("@/views/Login")},
    {
        path: '/Layout', name: 'Layout', component: Layout, children: [
            //
            {path: '/home', name: 'Home', component: () => import("@/views/Home")},
            {path: '/stuInfo', name: 'StuInfo', component: () => import("@/views/StuInfo")},
            {path: '/dormManagerInfo', name: 'DormManagerInfo', component: () => import("@/views/DormManagerInfo")},
            {path: '/buildingInfo', name: 'BuildingInfo', component: () => import("@/views/BuildingInfo")},
            {path: '/roomInfo', name: 'RoomInfo', component: () => import("@/views/RoomInfo")},
            {path: '/noticeInfo', name: 'NoticeInfo', component: () => import("@/views/NoticeInfo")},
            {path: '/adjustRoomInfo', name: 'AdjustRoomInfo', component: () => import("@/views/AdjustRoomInfo")},
            {path: '/repairInfo', name: 'RepairInfo', component: () => import("@/views/RepairInfo")},
            {path: '/visitorInfo', name: 'VisitorInfo', component: () => import("@/views/VisitorInfo")},
            //
            {path: '/myRoomInfo', name: 'MyRoomInfo', component: () => import("@/views/MyRoomInfo")},
            {path: '/applyRepairInfo', name: 'ApplyRepairInfo', component: () => import("@/views/ApplyRepairInfo")},
            {path: '/applyChangeRoom', name: 'ApplyChangeRoom', component: () => import("@/views/ApplyChangeRoom")},

            {path: '/selfInfo', name: 'SelfInfo', component: () => import("@/views/SelfInfo")},
        ]
    },

]
const router = createRouter({
    routes: constantRoutes,
    history: createWebHistory(process.env.BASE_URL)
})

router.beforeEach((to, from, next) => {
    const user = window.sessionStorage.getItem('user')
    const identity = window.sessionStorage.getItem('identity')

    if (to.path === '/Login') {
        return next()
    }

    if (!user || !identity) {
        return next('/Login')
    }

    if (to.path === '/') {
        return next('/home')
    }

    if (!hasPermission(identity, to.path)) {
        console.warn(`[权限拦截] 角色 ${identity} 无权访问 ${to.path}`)
        return next('/home')
    }

    next()
})

export default router
