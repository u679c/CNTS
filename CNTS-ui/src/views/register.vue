<template>
  <div class="register">
    <div class="register-shell">
      <section class="register-hero">
        <div class="hero-badge">Campus Network</div>
        <h1 class="hero-title">{{ title }}</h1>
        <p class="hero-subtitle">统一身份入口，开启可信连接体验</p>
        <div class="hero-metrics">
          <div class="metric">
            <span class="metric-label">服务</span>
            <span class="metric-value">一站式</span>
          </div>
          <div class="metric">
            <span class="metric-label">账号</span>
            <span class="metric-value">多角色</span>
          </div>
          <div class="metric">
            <span class="metric-label">保护</span>
            <span class="metric-value">多维度</span>
          </div>
        </div>
      </section>
      <section class="register-card">
        <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
          <h3 class="title">创建账号</h3>
          <p class="subtitle">填写以下信息完成注册</p>
          <el-form-item prop="username">
            <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="账号">
              <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="registerForm.password" type="password" auto-complete="off" placeholder="密码"
              @keyup.enter.native="handleRegister">
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input v-model="registerForm.confirmPassword" type="password" auto-complete="off" placeholder="确认密码"
              @keyup.enter.native="handleRegister">
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="code" v-if="captchaEnabled">
            <div class="captcha-row">
              <el-input v-model="registerForm.code" auto-complete="off" placeholder="验证码"
                @keyup.enter.native="handleRegister">
                <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
              </el-input>
              <div class="register-code">
                <img :src="codeUrl" @click="getCode" class="register-code-img" />
              </div>
            </div>
          </el-form-item>
          <div class="register-row">
            <span class="register-tip">注册即表示同意平台服务条款</span>
            <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
          </div>
          <el-form-item class="register-actions">
            <el-button :loading="loading" size="medium" type="primary" class="register-button"
              @click.native.prevent="handleRegister">
              <span v-if="!loading">注 册</span>
              <span v-else>注 册 中...</span>
            </el-button>
          </el-form-item>
        </el-form>
      </section>
    </div>
    <div class="el-register-footer">
      <span>{{ footerContent }}</span>
    </div>
  </div>
</template>

<script>
import { getCodeImg, register } from "@/api/login"
import defaultSettings from '@/settings'

export default {
  name: "Register",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"))
      } else {
        callback()
      }
    }
    return {
      title: process.env.VUE_APP_TITLE,
      footerContent: defaultSettings.footerContent,
      codeUrl: "",
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        code: "",
        uuid: ""
      },
      registerRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
          { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
          { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
          { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入您的密码" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      captchaEnabled: true
    }
  },
  created() {
    this.getCode()
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img
          this.registerForm.uuid = res.uuid
        }
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          register(this.registerForm).then(res => {
            const username = this.registerForm.username
            this.$alert("<font color='red'>恭喜你，您的账号 " + username + " 注册成功！</font>", '系统提示', {
              dangerouslyUseHTMLString: true,
              type: 'success'
            }).then(() => {
              this.$router.push("/login")
            }).catch(() => { })
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
.register {
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

.register::before {
  content: "";
  position: absolute;
  inset: 0;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
  background-position: center;
  opacity: 0.18;
  mix-blend-mode: screen;
}

.register::after {
  content: "";
  position: absolute;
  inset: 12% 8%;
  border-radius: 32px;
  border: 1px solid rgba(255, 255, 255, 0.12);
  background: linear-gradient(120deg, rgba(255, 255, 255, 0.08), transparent 60%);
  pointer-events: none;
}

.register-shell {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: minmax(260px, 1fr) minmax(320px, 420px);
  gap: 48px;
  width: min(980px, 100%);
  align-items: center;
  animation: fadeIn 0.7s ease-out;
}

.register-hero {
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

.register-card {
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

.register-form {
  display: grid;
  gap: 8px;
}

.register-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 6px 0 16px;
  font-size: 13px;
  color: #586878;
}

.register-actions {
  margin: 0;
}

.register-button {
  width: 100%;
  height: 44px;
  border-radius: 14px;
  font-weight: 600;
  letter-spacing: 3px;
  background: linear-gradient(135deg, #0b3b5a, #1f6aa2);
  border: none;
}

.captcha-row {
  display: flex;
  align-items: center;
  gap: 12px;
}

.register-code {
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

.register-tip {
  font-size: 12px;
  color: #8392a2;
}

.el-register-footer {
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
  .register-shell {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .register-hero {
    text-align: center;
    justify-items: center;
  }

  .hero-metrics {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .register-card {
    width: min(420px, 100%);
    justify-self: center;
  }
}

@media (max-width: 560px) {
  .register {
    padding: 48px 16px 72px;
  }

  .hero-metrics {
    grid-template-columns: 1fr;
  }
}
</style>
