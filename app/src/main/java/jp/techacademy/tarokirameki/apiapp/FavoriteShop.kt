package jp.techacademy.tarokirameki.apiapp

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*

open class FavoriteShop: RealmObject(), Serializable { // 課題:クーポン詳細ページでもお気に入りの追加削除
    @PrimaryKey
    var id: String = ""
    var imageUrl: String = ""
    var name: String = ""
    var url: String = ""
    var address: String = "" // 課題で追加、その際、Migrationについても触れてください。
    var deletedAt: Date? = null // 課題で追加、その際、Migrationについても触れてください。

    companion object {
        fun findAll(): List<FavoriteShop> = // お気に入りのShopを全件取得
            Realm.getDefaultInstance().use { realm ->
                realm.where(FavoriteShop::class.java)
                    .isNull(FavoriteShop::deletedAt.name) // 課題:物理削除から理論削除へ
                    .findAll().let {
                        realm.copyFromRealm(it)
                    }
            }

        fun findBy(id: String): FavoriteShop? = // お気に入りされているShopをidで検索して返す。お気に入りに登録されていなければnullで返す
            Realm.getDefaultInstance().use { realm ->
                realm.where(FavoriteShop::class.java)
                    .equalTo(FavoriteShop::id.name, id)
                    .isNull(FavoriteShop::deletedAt.name) // 課題:物理削除から理論削除へ
                    .findFirst()?.let {
                        realm.copyFromRealm(it)
                    }
            }

        fun insert(favoriteShop: FavoriteShop) = // お気に入り追加
            Realm.getDefaultInstance().executeTransaction {
                it.insertOrUpdate(favoriteShop)
            }

        fun delete(id: String) = // idでお気に入りから削除する
//            Realm.getDefaultInstance().use { realm ->
//                realm.where(FavoriteShop::class.java)
//                    .equalTo(FavoriteShop::id.name, id)
//                    .findFirst()?.also { deleteShop ->
//                        realm.executeTransaction {
//                            deleteShop.deleteFromRealm()
//                        }
//                    }
//            }
            findBy(id)?.also { // 課題:物理削除から理論削除へ
                insert(it.apply {
                    deletedAt = Date()
                })
            }
    }
}