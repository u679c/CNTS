<template>
  <div class="login">
    <div class="login-shell">
      <section class="login-hero">
        <div class="hero-badge">Campus Network</div>
        <h1 class="hero-title">{{ title }}</h1>
        <p class="hero-subtitle">可信认证与运维协同平台</p>
        <div class="hero-metrics">
          <div class="metric">
            <span class="metric-label">安全防护</span>
            <span class="metric-value">99.99%</span>
          </div>
          <div class="metric">
            <span class="metric-label">响应速度</span>
            <span class="metric-value">10分钟</span>
          </div>
          <div class="metric">
            <span class="metric-label">网络稳定</span>
            <span class="metric-value">毫秒级</span>
          </div>
        </div>
      </section>
      <section class="login-card">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
          <h3 class="title">欢迎回来</h3>
          <p class="subtitle">使用您的账号登录系统</p>
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号">
              <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码"
              @keyup.enter.native="handleLogin">
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="code" v-if="captchaEnabled"  >
          
            <div class="captcha-row">
              <el-input v-model="loginForm.code" auto-complete="off" placeholder="验证码"
                @keyup.enter.native="handleLogin">
                <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
              </el-input>
              <div class="login-code">
                <img :src="codeUrl" @click="getCode" class="login-code-img" />
              </div>
            </div>
          </el-form-item>
          <div class="login-row">
            <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
            <div v-if="register">
              <router-link class="link-type" :to="'/register'">立即注册</router-link>
            </div>
          </div>
          <el-form-item class="login-actions">
            <el-button :loading="loading" size="medium" type="primary" class="login-button"
              @click.native.prevent="handleLogin">
              <span v-if="!loading">登 录</span>
              <span v-else>登 录 中...</span>
            </el-button>
          </el-form-item>
        </el-form>
      </section>
    </div>
    <div class="el-login-footer">
      <span>{{ footerContent }}</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from '@/utils/jsencrypt'
import defaultSettings from '@/settings'

export default {
  name: "Login",
  data() {
    return {
      title: process.env.VUE_APP_TITLE,
      footerContent: defaultSettings.footerContent,
      codeUrl: "",
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: true,
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCode()
    this.getCookie()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    getCookie() {
      const username = Cookies.get("username")
      const password = Cookies.get("password")
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 })
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove("username")
            Cookies.remove("password")
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(() => { })
          }).catch(() => {
            this.loading = false
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  position: relative;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 64px 24px 80px;
  background:
    radial-gradient(circle at 10% 20%, rgba(18, 58, 89, 0.65), transparent 55%),
    radial-gradient(circle at 80% 0%, rgba(133, 196, 255, 0.45), transparent 40%),
    linear-gradient(135deg, #0b1a2a 0%, #0f2a3f 45%, #143f5b 100%);
  overflow: hidden;
  font-family: "Manrope", "Segoe UI", sans-serif;
}

.login::before {
  content: "";
  position: absolute;
  inset: 0;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
  background-position: center;
  opacity: 0.18;
  mix-blend-mode: screen;
}

.login::after {
  content: "";
  position: absolute;
  inset: 12% 8%;
  border-radius: 32px;
  border: 1px solid rgba(255, 255, 255, 0.12);
  background: linear-gradient(120deg, rgba(255, 255, 255, 0.08), transparent 60%);
  pointer-events: none;
}

.login-shell {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: minmax(260px, 1fr) minmax(320px, 420px);
  gap: 48px;
  width: min(980px, 100%);
  align-items: center;
  animation: fadeIn 0.7s ease-out;
}

.login-hero {
  color: #eaf3ff;
  display: grid;
  gap: 20px;
}

.hero-badge {
  align-self: flex-start;
  padding: 8px 16px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.16);
  letter-spacing: 2px;
  text-transform: uppercase;
  font-size: 12px;
  font-weight: 600;
}

.hero-title {
  font-family: "Cormorant Garamond", "Times New Roman", serif;
  font-size: clamp(34px, 4vw, 52px);
  letter-spacing: 1px;
  margin: 0;
}

.hero-subtitle {
  margin: 0;
  font-size: 16px;
  opacity: 0.85;
  max-width: 420px;
}

.hero-metrics {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
  margin-top: 12px;
}

.metric {
  padding: 14px 16px;
  border-radius: 14px;
  background: rgba(8, 20, 32, 0.55);
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.08);
  display: grid;
  gap: 6px;
}

.metric-label {
  font-size: 12px;
  letter-spacing: 2px;
  text-transform: uppercase;
  opacity: 0.65;
}

.metric-value {
  font-size: 20px;
  font-weight: 700;
}

.login-card {
  border-radius: 24px;
  padding: 34px 32px 28px;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 30px 60px rgba(7, 18, 35, 0.35);
  backdrop-filter: blur(12px);
}

.title {
  margin: 0;
  text-align: left;
  color: #0b1a2a;
  font-size: 26px;
  letter-spacing: 1px;
}

.subtitle {
  margin: 8px 0 22px;
  color: #516173;
  font-size: 14px;
}

.login-form {
  display: grid;
  gap: 8px;
}

.login-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 6px 0 16px;
  font-size: 13px;
  color: #586878;
}

.login-actions {
  margin: 0;
}

.login-button {
  width: 100%;
  height: 44px;
  border-radius: 14px;
  font-weight: 600;
  letter-spacing: 3px;
  background: linear-gradient(135deg, #0b3b5a, #1f6aa2);
  border: none;
}

.captcha-row {
  width:100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.login-code {
  width: 110px;
  height: 40px;
  border-radius: 10px;
  overflow: hidden;
  background: #f3f6fa;
  border: 1px solid rgba(11, 26, 42, 0.08);

  img {
    width: 100%;
    height: 100%;
    cursor: pointer;
    object-fit: cover;
  }
}

:deep(.el-input__inner) {
  height: 42px;
  border-radius: 12px;
  border: 1px solid rgba(11, 26, 42, 0.12);
  background: #f7f9fc;
}

:deep(.el-input__inner:focus) {
  border-color: rgba(31, 106, 162, 0.7);
  box-shadow: 0 0 0 3px rgba(31, 106, 162, 0.16);
}

:deep(.el-checkbox__label) {
  color: #5b6b7b;
}

.input-icon {
  height: 40px;
  width: 16px;
  margin-left: 6px;
  color: #7a8b9c;
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: rgba(255, 255, 255, 0.8);
  font-size: 12px;
  letter-spacing: 2px;
  text-transform: uppercase;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(12px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 960px) {
  .login-shell {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .login-hero {
    text-align: center;
    justify-items: center;
  }

  .hero-metrics {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .login-card {
    width: min(420px, 100%);
    justify-self: center;
  }
}

@media (max-width: 560px) {
  .login {
    padding: 48px 16px 72px;
  }

  .hero-metrics {
    grid-template-columns: 1fr;
  }

  .captcha-row {
    grid-template-columns: 1fr;
  }

  .login-code {
    width: 100%;
  }
}
</style>
