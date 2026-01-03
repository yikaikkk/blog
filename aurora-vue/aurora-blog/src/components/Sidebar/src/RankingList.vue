<template>
  <div class="sidebar-box mb-4">
    <SubTitle title="Ranking List" icon="hot" />
    <div class="bg-ob-deep-900 rounded-2xl shadow-xl p-4">
      <ul v-if="rankingArticles.length > 0" class="space-y-3 fade-in">
        <li 
          v-for="(article, index) in rankingArticles" 
          :key="article.articleId"
          class="flex items-center justify-between p-3 rounded-lg hover:bg-ob-deep-800 transition-all duration-300 cursor-pointer transform hover:scale-105 group"
          @click="goToArticle(article.articleId)">
          <div class="flex items-center space-x-3 flex-1 min-w-0">
            <span class="ranking-number flex-shrink-0" :class="getRankingClass(index)">
              {{ index + 1 }}
            </span>
            <div class="flex-1 min-w-0">
              <p class="text-sm font-medium text-ob-bright truncate hover:text-ob-secondary transition-colors cursor-pointer group-hover:text-ob-secondary" :title="article.articleTitle">
                {{ article.articleTitle }}
              </p>
              <div class="flex items-center justify-end mt-1">
                <div class="flex items-center text-xs text-ob-dimmer flex-shrink-0">
                  <svg-icon icon-class="eye" class="mr-1" />
                  {{ article.viewsCount || 0 }}
                </div>
              </div>
            </div>
          </div>
        </li>
      </ul>
      <div v-else class="text-center py-6 text-ob-secondary">
        <svg-icon icon-class="empty" class="text-3xl mb-3 opacity-50" />
        <p class="text-sm">暂无排行数据</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { SubTitle } from '@/components/Title'
import api from '@/api/api'

export default defineComponent({
  name: 'RankingList',
  components: { SubTitle },
  setup() {
    const router = useRouter()
    const rankingArticles = ref<Array<any>>([])

    const fetchRankingArticles = () => {
      // 首先尝试使用专门的排行榜接口
      api.getArticleTopList().then(({ data }) => {
        if (data.flag && data.data) {
          rankingArticles.value = data.data
        }
      }).catch(error => {
        console.log('排行榜接口不可用，回退到普通文章列表接口:', error)
        // 回退到使用普通文章列表接口
        api.getArticles({
          current: 1,
          size: 10,
          sort: 'viewsCount desc' // 按阅读量排序
        }).then(({ data }) => {
          if (data.flag && data.data.records) {
            rankingArticles.value = data.data.records
          }
        }).catch(fallbackError => {
          console.error('获取排行榜失败:', fallbackError)
        })
      })
    }

    const getRankingClass = (index: number) => {
      if (index === 0) return 'ranking-top-1'
      if (index === 1) return 'ranking-top-2'
      if (index === 2) return 'ranking-top-3'
      return 'ranking-normal'
    }

    const goToArticle = (articleId: number) => {
      // 使用正确的路由名称和参数跳转到文章详情页
      router.push({ 
        name: 'Articles', 
        params: { articleId: articleId.toString() }
      }).catch(err => {
        console.error('导航到文章详情页失败:', err)
        // 如果命名路由失败，尝试使用路径导航
        router.push(`/articles/${articleId}`).catch(pathErr => {
          console.error('路径导航也失败:', pathErr)
        })
      })
    }

    onMounted(() => {
      fetchRankingArticles()
    })

    return {
      rankingArticles,
      getRankingClass,
      goToArticle
    }
  }
})
</script>

<style lang="scss" scoped>
.ranking-number {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  font-size: 13px;
  font-weight: bold;
  color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  flex-shrink: 0;
}

.ranking-top-1 {
  background: linear-gradient(135deg, #ffd700, #ffed4e);
  color: #333;
  box-shadow: 0 4px 8px rgba(255, 215, 0, 0.3);
}

.ranking-top-2 {
  background: linear-gradient(135deg, #c0c0c0, #e8e8e8);
  color: #333;
  box-shadow: 0 4px 8px rgba(192, 192, 192, 0.3);
}

.ranking-top-3 {
  background: linear-gradient(135deg, #cd7f32, #e4a853);
  color: white;
  box-shadow: 0 4px 8px rgba(205, 127, 50, 0.3);
}

.ranking-normal {
  background: linear-gradient(135deg, #4a5568, #718096);
  color: white;
}

.text-ob-bright {
  color: #e2e8f0;
}

.text-ob-secondary {
  color: #a0aec0;
}

.text-ob-dimmer {
  color: #718096;
}

.bg-ob-deep-800 {
  background-color: #2d3748;
}

.bg-ob-deep-900 {
  background-color: #1a202c;
}

.sidebar-box {
  :deep(.ob-title) {
    margin-bottom: 1rem;
  }
}

.fade-in {
  animation: fadeIn 0.5s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 添加悬停效果
li:hover {
  .text-ob-bright {
    color: #f7fafc;
  }
  .text-ob-secondary {
    color: #e2e8f0;
  }
}
</style>