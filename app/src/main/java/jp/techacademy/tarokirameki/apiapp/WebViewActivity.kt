package jp.techacademy.tarokirameki.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity: AppCompatActivity() {

    private var favoriteShop: FavoriteShop = FavoriteShop() // 課題:クーポン詳細ページでもお気に入りの追加削除
    private var isFavorite = false // 課題:クーポン詳細ページでもお気に入りの追加削除
        set(value) {
            if (field == value)
                return
            field = value
            updateFavorite(value)
            invalidateOptionsMenu()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
//        webView.loadUrl(intent.getStringExtra(KEY_URL))
        // 課題:クーポン詳細ページでもお気に入りの追加削除
        favoriteShop = intent.getSerializableExtra(KEY_FAVORITE_SHOP) as? FavoriteShop ?: return { finish() }()
        webView.loadUrl(favoriteShop.url)
        isFavorite = FavoriteShop.findBy(favoriteShop.id) != null
    }

    // 課題:クーポン詳細ページでもお気に入りの追加削除
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.meue_web_view_activity, menu)
        menu?.findItem(R.id.actionFavorite)?.setIcon(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border)
        return true
    }

    // 課題:クーポン詳細ページでもお気に入りの追加削除
    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when(item.itemId) {
            R.id.actionFavorite -> {
                isFavorite = !isFavorite
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    // 課題:クーポン詳細ページでもお気に入りの追加削除
    private fun updateFavorite(willFavorite: Boolean) {
        if (willFavorite)
            FavoriteShop.insert(favoriteShop)
        else
            FavoriteShop.delete(favoriteShop.id)
    }

    companion object {
        private const val KEY_URL = "key_url"
        private const val KEY_FAVORITE_SHOP = "key_favorite_shop" // 課題:クーポン詳細ページでもお気に入りの追加削除

        fun start(activity: Activity, favoriteShop: FavoriteShop) { // 課題:クーポン詳細ページでもお気に入りの追加削除
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_FAVORITE_SHOP, favoriteShop))
        }

        fun start(activity: Activity, url: String) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL, url))
        }
    }
}