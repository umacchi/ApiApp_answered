package jp.techacademy.tarokirameki.apiapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ApiAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Shop>()

    var onClickAddFavorite: ((Shop) -> Unit)? = null // Favoriteに追加するときのメソッド(Adapter -> Fragment へ通知する)
    var onClickDeleteFavorite: ((Shop) -> Unit)? = null // Favoriteから削除するときのメソッド(Adapter -> Fragment へ通知する)
//    var onClickItem: ((String) -> Unit)? = null // Itemを押したときのメソッド
    var onClickItem: ((Shop) -> Unit)? = null // 課題:クーポン詳細ページでもお気に入りの追加削除

    fun refresh(list: List<Shop>) {
        update(list, false)
    }

    fun add(list: List<Shop>) {
        update(list, true)
    }

    private fun update(list: List<Shop>, isAdd: Boolean) {
        items.apply {
            if (!isAdd) { // 追加の時はclearしない
                clear() // items を 空にする
            }
            addAll(list) // itemsにlistを全て追加する
        }
        notifyDataSetChanged() // recyclerViewを再描画させる
    }

    override fun getItemCount(): Int {
//        return items.size
        return if (items.isEmpty()) 1 else items.size // 課題:キーワード検索
    }

    // 課題:キーワード検索
    override fun getItemViewType(position: Int): Int {
        return if (items.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_EMPTY -> EmptyViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_api_empty, parent, false))
            else -> ApiItemViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_api, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ApiItemViewHolder) {
            updateApiItemViewHolder(holder, position)
        }
    }

    private fun updateApiItemViewHolder(holder: ApiItemViewHolder, position: Int) {
        val data = items[position]
        val isFavorite = FavoriteShop.findBy(data.id) != null // お気に入り状態を取得
        holder.apply {
            rootView.apply {
                setBackgroundColor(ContextCompat.getColor(context, if (position % 2 == 0) android.R.color.white else android.R.color.darker_gray)) // 偶数番目と機数番目で背景色を変更させる
                setOnClickListener {
//                    onClickItem?.invoke(if (data.couponUrls.sp.isNotEmpty()) data.couponUrls.sp else data.couponUrls.pc)
                    onClickItem?.invoke(data) // // 課題:クーポン詳細ページでもお気に入りの追加削除
                }
            }
            nameTextView.text = data.name
            addressTextView.text = data.address
            Picasso.get().load(data.logoImage).into(imageView)
            favoriteImageView.apply {
                setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border) // Picassoというライブラリを使ってImageVIewに画像をはめ込む
                setOnClickListener {
                    if (isFavorite) {
                        onClickDeleteFavorite?.invoke(data)
                    } else {
                        onClickAddFavorite?.invoke(data)
                    }
                    notifyItemChanged(position)
                }
            }
        }
    }

    class ApiItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val rootView : ConstraintLayout = view.findViewById(R.id.rootView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val addressTextView: TextView = view.findViewById(R.id.addressTextView)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val favoriteImageView: ImageView = view.findViewById(R.id.favoriteImageView)
    }

    class EmptyViewHolder(view: View): RecyclerView.ViewHolder(view)

    companion object {
        // 課題:キーワード検索
        private const val VIEW_TYPE_EMPTY = 0
        private const val VIEW_TYPE_ITEM = 1
    }
}