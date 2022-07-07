package jp.techacademy.tarokirameki.apiapp

interface FragmentCallback {
    // Itemを押したときの処理
//    fun onClickItem(url: String)
    fun onClickItem(favoriteShop: FavoriteShop) // 課題:クーポン詳細ページでもお気に入りの追加削除
    fun onClickItem(shop: Shop) // 課題:クーポン詳細ページでもお気に入りの追加削除
    // お気に入り追加時の処理
    fun onAddFavorite(shop: Shop)
    // お気に入り削除時の処理
    fun onDeleteFavorite(id: String)
}