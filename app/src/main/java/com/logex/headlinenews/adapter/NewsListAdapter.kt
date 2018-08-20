package com.logex.headlinenews.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.logex.adapter.recyclerview.CommonAdapter
import com.logex.adapter.recyclerview.base.ItemViewDelegate
import com.logex.adapter.recyclerview.base.ViewHolder
import com.logex.headlinenews.R
import com.logex.headlinenews.model.NewsListEntity
import com.logex.headlinenews.utils.TimeFormatUtil.Companion.getPublishTime
import com.logex.utils.ValidateUtil


/**
 * 创建人: liguangxi
 * 日期: 2018/2/23
 * 邮箱: 956328710@qq.com
 * 版本: 1.0
 * 新闻列表适配器
 */
class NewsListAdapter(context: Context, list: List<NewsListEntity.Content>, layoutId: Int) : CommonAdapter<NewsListEntity.Content>(context, list, layoutId) {

    init {
        // 添加单图type
        addItemViewDelegate(object : ItemViewDelegate<NewsListEntity.Content> {

            override fun getItemViewLayoutId(): Int = R.layout.recycler_item_news_single_image

            override fun isForViewType(item: NewsListEntity.Content, position: Int): Boolean =
                    item.has_image && (item.image_list == null || item.image_list.isEmpty())

            override fun convert(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) =
                    convertSingleImageView(viewHolder, item, position)

        })

        // 添加一张大图type
        addItemViewDelegate(object : ItemViewDelegate<NewsListEntity.Content> {

            override fun getItemViewLayoutId(): Int = R.layout.recycler_item_news_single_big_image

            override fun isForViewType(item: NewsListEntity.Content, position: Int): Boolean =
                    item.has_image && item.image_list != null && item.image_list.size <= 1

            override fun convert(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) =
                    convertSingleBigImageView(viewHolder, item, position)

        })

        // 添加多图type
        addItemViewDelegate(object : ItemViewDelegate<NewsListEntity.Content> {

            override fun getItemViewLayoutId(): Int = R.layout.recycler_item_news_multiple_image

            override fun isForViewType(item: NewsListEntity.Content, position: Int): Boolean =
                    item.has_image && item.image_list != null && item.image_list.size > 1

            override fun convert(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) =
                    convertMultipleView(viewHolder, item, position)

        })

        // 添加广告大图type
        addItemViewDelegate(object : ItemViewDelegate<NewsListEntity.Content> {

            override fun getItemViewLayoutId(): Int = R.layout.recycler_item_news_ad_big_image

            override fun isForViewType(item: NewsListEntity.Content, position: Int): Boolean =
                    item.ad_id != null && item.download_url == null && !item.has_video &&
                            (item.image_list == null || item.image_list.isEmpty())

            override fun convert(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) =
                    convertADBigImageView(viewHolder, item, position)

        })

        // 添加广告多图type
        addItemViewDelegate(object : ItemViewDelegate<NewsListEntity.Content> {

            override fun getItemViewLayoutId(): Int = R.layout.recycler_item_news_ad_multiple_image

            override fun isForViewType(item: NewsListEntity.Content, position: Int): Boolean =
                    item.ad_id != null && item.download_url == null && !item.has_video &&
                            item.image_list != null && item.image_list.isNotEmpty()

            override fun convert(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) =
                    convertADMultipleImageView(viewHolder, item, position)

        })

        // 添加广告 有视频 带app下载type
        addItemViewDelegate(object : ItemViewDelegate<NewsListEntity.Content> {

            override fun getItemViewLayoutId(): Int = R.layout.recycler_item_news_ad_video_app

            override fun isForViewType(item: NewsListEntity.Content, position: Int): Boolean =
                    item.ad_id != null && item.has_video

            override fun convert(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) =
                    convertADHasVideoAppView(viewHolder, item, position)

        })

        // 添加广告大图 带app下载type
        addItemViewDelegate(object : ItemViewDelegate<NewsListEntity.Content> {

            override fun getItemViewLayoutId(): Int = R.layout.recycler_item_news_ad_big_image_app

            override fun isForViewType(item: NewsListEntity.Content, position: Int): Boolean =
                    item.ad_id != null && item.download_url != null && !item.has_video &&
                            (item.image_list == null || item.image_list.isEmpty())

            override fun convert(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) =
                    convertADBigImageAppView(viewHolder, item, position)

        })

        // 添加广告多图 带app下载type
        addItemViewDelegate(object : ItemViewDelegate<NewsListEntity.Content> {

            override fun getItemViewLayoutId(): Int = R.layout.recycler_item_news_ad_multiple_image_app

            override fun isForViewType(item: NewsListEntity.Content, position: Int): Boolean =
                    item.ad_id != null && item.download_url != null && !item.has_video &&
                            item.image_list != null && item.image_list.isNotEmpty()

            override fun convert(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) =
                    convertADMultipleImageAppView(viewHolder, item, position)

        })

        // 添加视频 单图type
        addItemViewDelegate(object : ItemViewDelegate<NewsListEntity.Content> {

            override fun getItemViewLayoutId(): Int = R.layout.recycler_item_news_video_single_image

            override fun isForViewType(item: NewsListEntity.Content, position: Int): Boolean =
                    !item.has_image && item.ad_id == null && item.has_video &&
                            (item.image_list == null || item.image_list.isEmpty())

            override fun convert(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) =
                    convertVideoSingleImageView(viewHolder, item, position)

        })

        // 添加视频 大图type
        addItemViewDelegate(object : ItemViewDelegate<NewsListEntity.Content> {

            override fun getItemViewLayoutId(): Int = R.layout.recycler_item_news_video_big_image

            override fun isForViewType(item: NewsListEntity.Content, position: Int): Boolean =
                    !item.has_image && item.ad_id == null && item.has_video &&
                            (item.image_list == null || item.image_list.isEmpty())

            override fun convert(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) =
                    convertVideoBigImageView(viewHolder, item, position)

        })
    }

    private fun convertVideoSingleImageView(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) {
        viewHolder.setText(R.id.tv_news_title, item.title)

        val middleImage = item.middle_image

        if (middleImage != null) {
            viewHolder.setImageResourcesUrl(R.id.iv_video_thumbnail, middleImage.url, -1)
        }

        viewHolder.setText(R.id.tv_news_source, item.source)

        viewHolder.setText(R.id.tv_comment_count, "${item.comment_count}评论")

        viewHolder.setText(R.id.tv_video_duration, "02:33")
    }

    private fun convertVideoBigImageView(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) {
        viewHolder.setText(R.id.tv_news_title, item.title)

        val largeImageList = item.large_image_list

        if (ValidateUtil.isListNonEmpty(largeImageList)) {
            val largeImage = largeImageList!![0]
            viewHolder.setImageResourcesUrl(R.id.iv_video_thumbnail, largeImage.url, -1)
        }

        viewHolder.setText(R.id.tv_news_source, item.source)

        viewHolder.setText(R.id.tv_comment_count, "${item.comment_count}评论")

        viewHolder.setText(R.id.tv_news_time, getPublishTime(item.publish_time))
    }

    private fun convertADMultipleImageAppView(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) {
        viewHolder.setText(R.id.tv_news_title, item.title)

        // 显示图片
        val imageList: List<NewsListEntity.Image>? = item.image_list
        val rvNewsPicture: RecyclerView = viewHolder.getView(R.id.rv_news_picture)
        val gridManager = GridLayoutManager(mContext, 3)
        rvNewsPicture.layoutManager = gridManager
        val pictureAdapter = NewsMultiplePictureAdapter(mContext, imageList,
                R.layout.recycler_item_news_picture_list_view, item.gallary_image_count)
        rvNewsPicture.adapter = pictureAdapter

        viewHolder.setText(R.id.tv_news_subtitle, item.sub_title)

        viewHolder.setText(R.id.tv_news_source, item.source)

        viewHolder.setText(R.id.tv_news_time, getPublishTime(item.publish_time))
    }

    private fun convertADBigImageAppView(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) {
        viewHolder.setText(R.id.tv_news_title, item.title)

        val image = item.image

        if (image != null) {
            viewHolder.setImageResourcesUrl(R.id.iv_news_img, image.url, -1)
        }

        viewHolder.setText(R.id.tv_news_subtitle, item.sub_title)

        viewHolder.setText(R.id.tv_news_source, item.source)

        viewHolder.setText(R.id.tv_news_time, getPublishTime(item.publish_time))
    }

    private fun convertADHasVideoAppView(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) {
        viewHolder.setText(R.id.tv_news_title, item.title)

        val largeImageList = item.large_image_list

        if (ValidateUtil.isListNonEmpty(largeImageList)) {
            val largeImage = largeImageList!![0]
            viewHolder.setImageResourcesUrl(R.id.iv_video_thumbnail, largeImage.url, -1)
        }

        viewHolder.setText(R.id.tv_news_subtitle, item.sub_title)

        viewHolder.setText(R.id.tv_news_source, item.source)

        viewHolder.setText(R.id.tv_news_time, getPublishTime(item.publish_time))
    }

    private fun convertADMultipleImageView(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) {
        viewHolder.setText(R.id.tv_news_title, item.title)

        // 显示图片
        val imageList: List<NewsListEntity.Image>? = item.image_list
        val rvNewsPicture: RecyclerView = viewHolder.getView(R.id.rv_news_picture)
        val gridManager = GridLayoutManager(mContext, 3)
        rvNewsPicture.layoutManager = gridManager
        val pictureAdapter = NewsMultiplePictureAdapter(mContext, imageList,
                R.layout.recycler_item_news_picture_list_view, item.gallary_image_count)
        rvNewsPicture.adapter = pictureAdapter

        viewHolder.setText(R.id.tv_news_source, item.source)

        viewHolder.setText(R.id.tv_news_time, getPublishTime(item.publish_time))
    }

    private fun convertADBigImageView(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) {
        viewHolder.setText(R.id.tv_news_title, item.title)

        val largeImageList = item.large_image_list

        if (ValidateUtil.isListNonEmpty(largeImageList)) {
            val largeImage = largeImageList!![0]
            viewHolder.setImageResourcesUrl(R.id.iv_news_img, largeImage.url, -1)
        }

        viewHolder.setText(R.id.tv_news_source, item.source)

        viewHolder.setText(R.id.tv_news_time, getPublishTime(item.publish_time))
    }

    private fun convertMultipleView(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) {
        viewHolder.setText(R.id.tv_news_title, item.title)

        // 显示图片
        val imageList: List<NewsListEntity.Image>? = item.image_list
        val rvNewsPicture: RecyclerView = viewHolder.getView(R.id.rv_news_picture)
        val gridManager = GridLayoutManager(mContext, 3)
        rvNewsPicture.layoutManager = gridManager
        val pictureAdapter = NewsMultiplePictureAdapter(mContext, imageList,
                R.layout.recycler_item_news_picture_list_view, item.gallary_image_count)
        rvNewsPicture.adapter = pictureAdapter

        viewHolder.setText(R.id.tv_news_source, item.media_name)

        viewHolder.setText(R.id.tv_comment_count, "${item.comment_count}评论")
    }

    private fun convertSingleBigImageView(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) {
        viewHolder.setText(R.id.tv_news_title, item.title)

        viewHolder.setText(R.id.tv_news_source, item.source)

        viewHolder.setText(R.id.tv_comment_count, "${item.comment_count}评论")

        // 显示图片
        val image = item.image_list!![0]

        viewHolder.setImageResourcesUrl(R.id.iv_news_img, image.url, R.drawable.bg_new_list_image)

        viewHolder.setText(R.id.tv_news_image_size, "${item.gallary_image_count}图")
    }

    private fun convertSingleImageView(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) {
        viewHolder.setText(R.id.tv_news_title, item.title)

        viewHolder.setText(R.id.tv_news_source, item.source)

        viewHolder.setText(R.id.tv_comment_count, "${item.comment_count}评论")

        val middleImage = item.middle_image

        if (middleImage != null) {
            viewHolder.setImageResourcesUrl(R.id.iv_news_img, middleImage.url, R.drawable.bg_new_list_image)
        }
    }

    override fun convertView(viewHolder: ViewHolder, item: NewsListEntity.Content, position: Int) {
        viewHolder.setText(R.id.tv_news_title, item.title)

        viewHolder.setText(R.id.tv_news_source, item.source)

        viewHolder.setText(R.id.tv_comment_count, "${item.comment_count}评论")

        viewHolder.setText(R.id.tv_news_time, getPublishTime(item.publish_time))
    }
}