
import com.google.gson.annotations.SerializedName
data class ListingResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("kind")
    var kind: String = ""
)

data class Data(
    @SerializedName("after")
    var after: String = "",
    @SerializedName("before")
    var before: String? = null,
    @SerializedName("children")
    var children: List<Children> = listOf(),
    @SerializedName("dist")
    var dist: Int = 0,
    @SerializedName("geo_filter")
    var geoFilter: Any? = null,
    @SerializedName("modhash")
    var modhash: String = ""
)

data class Children(
    @SerializedName("data")
    var `data`: PostData = PostData(),
    @SerializedName("kind")
    var kind: String = ""
)

data class PostData(
    @SerializedName("all_awardings")
    var allAwardings: List<AllAwarding> = listOf(),
    @SerializedName("allow_live_comments")
    var allowLiveComments: Boolean = false,
    @SerializedName("approved_at_utc")
    var approvedAtUtc: Any? = null,
    @SerializedName("approved_by")
    var approvedBy: Any? = null,
    @SerializedName("archived")
    var archived: Boolean = false,
    @SerializedName("author")
    var author: String = "",
    @SerializedName("author_cakeday")
    var authorCakeday: Boolean = false,
    @SerializedName("author_flair_background_color")
    var authorFlairBackgroundColor: Any? = null,
    @SerializedName("author_flair_css_class")
    var authorFlairCssClass: Any? = null,
    @SerializedName("author_flair_richtext")
    var authorFlairRichtext: List<AuthorFlairRichtext> = listOf(),
    @SerializedName("author_flair_template_id")
    var authorFlairTemplateId: Any? = null,
    @SerializedName("author_flair_text")
    var authorFlairText: Any? = null,
    @SerializedName("author_flair_text_color")
    var authorFlairTextColor: Any? = null,
    @SerializedName("author_flair_type")
    var authorFlairType: String = "",
    @SerializedName("author_fullname")
    var authorFullname: String = "",
    @SerializedName("author_is_blocked")
    var authorIsBlocked: Boolean = false,
    @SerializedName("author_patreon_flair")
    var authorPatreonFlair: Boolean = false,
    @SerializedName("author_premium")
    var authorPremium: Boolean = false,
    @SerializedName("awarders")
    var awarders: List<Any> = listOf(),
    @SerializedName("banned_at_utc")
    var bannedAtUtc: Any? = null,
    @SerializedName("banned_by")
    var bannedBy: Any? = null,
    @SerializedName("can_gild")
    var canGild: Boolean = false,
    @SerializedName("can_mod_post")
    var canModPost: Boolean = false,
    @SerializedName("category")
    var category: Any? = null,
    @SerializedName("clicked")
    var clicked: Boolean = false,
    @SerializedName("content_categories")
    var contentCategories: Any? = null,
    @SerializedName("contest_mode")
    var contestMode: Boolean = false,
    @SerializedName("created")
    var created: Int = 0,
    @SerializedName("created_utc")
    var createdUtc: Int = 0,
    @SerializedName("discussion_type")
    var discussionType: Any? = null,
    @SerializedName("distinguished")
    var distinguished: Any? = null,
    @SerializedName("domain")
    var domain: String = "",
    @SerializedName("downs")
    var downs: Int = 0,
    @SerializedName("edited")
    var edited: Any? = null,
    @SerializedName("gallery_data")
    var galleryData: GalleryData = GalleryData(),
    @SerializedName("gilded")
    var gilded: Int = 0,
    @SerializedName("gildings")
    var gildings: Gildings = Gildings(),
    @SerializedName("hidden")
    var hidden: Boolean = false,
    @SerializedName("hide_score")
    var hideScore: Boolean = false,
    @SerializedName("id")
    var id: String = "",
    @SerializedName("is_created_from_ads_ui")
    var isCreatedFromAdsUi: Boolean = false,
    @SerializedName("is_crosspostable")
    var isCrosspostable: Boolean = false,
    @SerializedName("is_gallery")
    var isGallery: Boolean = false,
    @SerializedName("is_meta")
    var isMeta: Boolean = false,
    @SerializedName("is_original_content")
    var isOriginalContent: Boolean = false,
    @SerializedName("is_reddit_media_domain")
    var isRedditMediaDomain: Boolean = false,
    @SerializedName("is_robot_indexable")
    var isRobotIndexable: Boolean = false,
    @SerializedName("is_self")
    var isSelf: Boolean = false,
    @SerializedName("is_video")
    var isVideo: Boolean = false,
    @SerializedName("likes")
    var likes: Any? = null,
    @SerializedName("link_flair_background_color")
    var linkFlairBackgroundColor: String = "",
    @SerializedName("link_flair_css_class")
    var linkFlairCssClass: String? = null,
    @SerializedName("link_flair_richtext")
    var linkFlairRichtext: List<LinkFlairRichtext> = listOf(),
    @SerializedName("link_flair_template_id")
    var linkFlairTemplateId: String = "",
    @SerializedName("link_flair_text")
    var linkFlairText: String? = null,
    @SerializedName("link_flair_text_color")
    var linkFlairTextColor: String = "",
    @SerializedName("link_flair_type")
    var linkFlairType: String = "",
    @SerializedName("locked")
    var locked: Boolean = false,
    @SerializedName("media")
    var media: Media? = null,
    @SerializedName("media_embed")
    var mediaEmbed: MediaEmbed = MediaEmbed(),
    @SerializedName("media_metadata")
    var mediaMetadata: MediaMetadata = MediaMetadata(),
    @SerializedName("media_only")
    var mediaOnly: Boolean = false,
    @SerializedName("mod_note")
    var modNote: Any? = null,
    @SerializedName("mod_reason_by")
    var modReasonBy: Any? = null,
    @SerializedName("mod_reason_title")
    var modReasonTitle: Any? = null,
    @SerializedName("mod_reports")
    var modReports: List<Any> = listOf(),
    @SerializedName("name")
    var name: String = "",
    @SerializedName("no_follow")
    var noFollow: Boolean = false,
    @SerializedName("num_comments")
    var numComments: Int = 0,
    @SerializedName("num_crossposts")
    var numCrossposts: Int = 0,
    @SerializedName("num_reports")
    var numReports: Any? = null,
    @SerializedName("over_18")
    var over18: Boolean = false,
    @SerializedName("parent_whitelist_status")
    var parentWhitelistStatus: String = "",
    @SerializedName("permalink")
    var permalink: String = "",
    @SerializedName("pinned")
    var pinned: Boolean = false,
    @SerializedName("post_hint")
    var postHint: String = "",
    @SerializedName("preview")
    var preview: Preview = Preview(),
    @SerializedName("pwls")
    var pwls: Int = 0,
    @SerializedName("quarantine")
    var quarantine: Boolean = false,
    @SerializedName("removal_reason")
    var removalReason: Any? = null,
    @SerializedName("removed_by")
    var removedBy: Any? = null,
    @SerializedName("removed_by_category")
    var removedByCategory: Any? = null,
    @SerializedName("report_reasons")
    var reportReasons: Any? = null,
    @SerializedName("saved")
    var saved: Boolean = false,
    @SerializedName("score")
    var score: Int = 0,
    @SerializedName("secure_media")
    var secureMedia: SecureMedia? = null,
    @SerializedName("secure_media_embed")
    var secureMediaEmbed: SecureMediaEmbed = SecureMediaEmbed(),
    @SerializedName("selftext")
    var selftext: String = "",
    @SerializedName("selftext_html")
    var selftextHtml: Any? = null,
    @SerializedName("send_replies")
    var sendReplies: Boolean = false,
    @SerializedName("spoiler")
    var spoiler: Boolean = false,
    @SerializedName("stickied")
    var stickied: Boolean = false,
    @SerializedName("subreddit")
    var subreddit: String = "",
    @SerializedName("subreddit_id")
    var subredditId: String = "",
    @SerializedName("subreddit_name_prefixed")
    var subredditNamePrefixed: String = "",
    @SerializedName("subreddit_subscribers")
    var subredditSubscribers: Int = 0,
    @SerializedName("subreddit_type")
    var subredditType: String = "",
    @SerializedName("suggested_sort")
    var suggestedSort: Any? = null,
    @SerializedName("thumbnail")
    var thumbnail: String = "",
    @SerializedName("thumbnail_height")
    var thumbnailHeight: Int? = null,
    @SerializedName("thumbnail_width")
    var thumbnailWidth: Int? = null,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("top_awarded_type")
    var topAwardedType: String? = null,
    @SerializedName("total_awards_received")
    var totalAwardsReceived: Int = 0,
    @SerializedName("treatment_tags")
    var treatmentTags: List<Any> = listOf(),
    @SerializedName("ups")
    var ups: Int = 0,
    @SerializedName("upvote_ratio")
    var upvoteRatio: Double = 0.0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("url_overridden_by_dest")
    var urlOverriddenByDest: String = "",
    @SerializedName("user_reports")
    var userReports: List<Any> = listOf(),
    @SerializedName("view_count")
    var viewCount: Any? = null,
    @SerializedName("visited")
    var visited: Boolean = false,
    @SerializedName("whitelist_status")
    var whitelistStatus: String = "",
    @SerializedName("wls")
    var wls: Int = 0
)

data class AllAwarding(
    @SerializedName("award_sub_type")
    var awardSubType: String = "",
    @SerializedName("award_type")
    var awardType: String = "",
    @SerializedName("awardings_required_to_grant_benefits")
    var awardingsRequiredToGrantBenefits: Any? = null,
    @SerializedName("coin_price")
    var coinPrice: Int = 0,
    @SerializedName("coin_reward")
    var coinReward: Int = 0,
    @SerializedName("count")
    var count: Int = 0,
    @SerializedName("days_of_drip_extension")
    var daysOfDripExtension: Int = 0,
    @SerializedName("days_of_premium")
    var daysOfPremium: Int = 0,
    @SerializedName("description")
    var description: String = "",
    @SerializedName("end_date")
    var endDate: Any? = null,
    @SerializedName("giver_coin_reward")
    var giverCoinReward: Int? = null,
    @SerializedName("icon_format")
    var iconFormat: String? = null,
    @SerializedName("icon_height")
    var iconHeight: Int = 0,
    @SerializedName("icon_url")
    var iconUrl: String = "",
    @SerializedName("icon_width")
    var iconWidth: Int = 0,
    @SerializedName("id")
    var id: String = "",
    @SerializedName("is_enabled")
    var isEnabled: Boolean = false,
    @SerializedName("is_new")
    var isNew: Boolean = false,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("penny_donate")
    var pennyDonate: Int? = null,
    @SerializedName("penny_price")
    var pennyPrice: Int? = null,
    @SerializedName("resized_icons")
    var resizedIcons: List<ResizedIcon> = listOf(),
    @SerializedName("resized_static_icons")
    var resizedStaticIcons: List<ResizedStaticIcon> = listOf(),
    @SerializedName("start_date")
    var startDate: Any? = null,
    @SerializedName("static_icon_height")
    var staticIconHeight: Int = 0,
    @SerializedName("static_icon_url")
    var staticIconUrl: String = "",
    @SerializedName("static_icon_width")
    var staticIconWidth: Int = 0,
    @SerializedName("subreddit_coin_reward")
    var subredditCoinReward: Int = 0,
    @SerializedName("subreddit_id")
    var subredditId: Any? = null,
    @SerializedName("tiers_by_required_awardings")
    var tiersByRequiredAwardings: Any? = null
)

data class AuthorFlairRichtext(
    @SerializedName("e")
    var e: String = "",
    @SerializedName("t")
    var t: String = ""
)

data class GalleryData(
    @SerializedName("items")
    var items: List<Item> = listOf()
)

data class Gildings(
    @SerializedName("gid_1")
    var gid1: Int = 0,
    @SerializedName("gid_2")
    var gid2: Int = 0
)

data class LinkFlairRichtext(
    @SerializedName("a")
    var a: String = "",
    @SerializedName("e")
    var e: String = "",
    @SerializedName("t")
    var t: String = "",
    @SerializedName("u")
    var u: String = ""
)

data class Media(
    @SerializedName("oembed")
    var oembed: Oembed = Oembed(),
    @SerializedName("reddit_video")
    var redditVideo: RedditVideo = RedditVideo(),
    @SerializedName("type")
    var type: String = ""
)

data class MediaEmbed(
    @SerializedName("content")
    var content: String = "",
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("scrolling")
    var scrolling: Boolean = false,
    @SerializedName("width")
    var width: Int = 0
)

data class MediaMetadata(
    @SerializedName("15dogmcb04c81")
    var dogmcb04c81: Dogmcb04c81 = Dogmcb04c81(),
    @SerializedName("l48vb3cb04c81")
    var l48vb3cb04c81: L48vb3cb04c81 = L48vb3cb04c81()
)

data class Preview(
    @SerializedName("enabled")
    var enabled: Boolean = false,
    @SerializedName("images")
    var images: List<Image> = listOf(),
    @SerializedName("reddit_video_preview")
    var redditVideoPreview: RedditVideoPreview = RedditVideoPreview()
)

data class SecureMedia(
    @SerializedName("oembed")
    var oembed: SecureMediaOembed = SecureMediaOembed(),
    @SerializedName("reddit_video")
    var redditVideo: SecureMediaRedditVideo = SecureMediaRedditVideo(),
    @SerializedName("type")
    var type: String = ""
)

data class SecureMediaEmbed(
    @SerializedName("content")
    var content: String = "",
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("media_domain_url")
    var mediaDomainUrl: String = "",
    @SerializedName("scrolling")
    var scrolling: Boolean = false,
    @SerializedName("width")
    var width: Int = 0
)

data class ResizedIcon(
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class ResizedStaticIcon(
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class Item(
    @SerializedName("caption")
    var caption: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("media_id")
    var mediaId: String = ""
)

data class Oembed(
    @SerializedName("author_name")
    var authorName: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("html")
    var html: String = "",
    @SerializedName("provider_name")
    var providerName: String = "",
    @SerializedName("provider_url")
    var providerUrl: String = "",
    @SerializedName("thumbnail_height")
    var thumbnailHeight: Int = 0,
    @SerializedName("thumbnail_url")
    var thumbnailUrl: String = "",
    @SerializedName("thumbnail_width")
    var thumbnailWidth: Int = 0,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("version")
    var version: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class RedditVideo(
    @SerializedName("bitrate_kbps")
    var bitrateKbps: Int = 0,
    @SerializedName("dash_url")
    var dashUrl: String = "",
    @SerializedName("duration")
    var duration: Int = 0,
    @SerializedName("fallback_url")
    var fallbackUrl: String = "",
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("hls_url")
    var hlsUrl: String = "",
    @SerializedName("is_gif")
    var isGif: Boolean = false,
    @SerializedName("scrubber_media_url")
    var scrubberMediaUrl: String = "",
    @SerializedName("transcoding_status")
    var transcodingStatus: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class Dogmcb04c81(
    @SerializedName("e")
    var e: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("m")
    var m: String = "",
    @SerializedName("p")
    var p: List<P> = listOf(),
    @SerializedName("s")
    var s: S = S(),
    @SerializedName("status")
    var status: String = ""
)

data class L48vb3cb04c81(
    @SerializedName("e")
    var e: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("m")
    var m: String = "",
    @SerializedName("p")
    var p: List<PX> = listOf(),
    @SerializedName("s")
    var s: SX = SX(),
    @SerializedName("status")
    var status: String = ""
)

data class P(
    @SerializedName("u")
    var u: String = "",
    @SerializedName("x")
    var x: Int = 0,
    @SerializedName("y")
    var y: Int = 0
)

data class S(
    @SerializedName("u")
    var u: String = "",
    @SerializedName("x")
    var x: Int = 0,
    @SerializedName("y")
    var y: Int = 0
)

data class PX(
    @SerializedName("u")
    var u: String = "",
    @SerializedName("x")
    var x: Int = 0,
    @SerializedName("y")
    var y: Int = 0
)

data class SX(
    @SerializedName("u")
    var u: String = "",
    @SerializedName("x")
    var x: Int = 0,
    @SerializedName("y")
    var y: Int = 0
)

data class Image(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("resolutions")
    var resolutions: List<Resolution> = listOf(),
    @SerializedName("source")
    var source: Source = Source(),
    @SerializedName("variants")
    var variants: Variants = Variants()
)

data class RedditVideoPreview(
    @SerializedName("bitrate_kbps")
    var bitrateKbps: Int = 0,
    @SerializedName("dash_url")
    var dashUrl: String = "",
    @SerializedName("duration")
    var duration: Int = 0,
    @SerializedName("fallback_url")
    var fallbackUrl: String = "",
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("hls_url")
    var hlsUrl: String = "",
    @SerializedName("is_gif")
    var isGif: Boolean = false,
    @SerializedName("scrubber_media_url")
    var scrubberMediaUrl: String = "",
    @SerializedName("transcoding_status")
    var transcodingStatus: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class Resolution(
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class Source(
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class Variants(
    @SerializedName("gif")
    var gif: Gif = Gif(),
    @SerializedName("mp4")
    var mp4: Mp4 = Mp4()
)

data class Gif(
    @SerializedName("resolutions")
    var resolutions: List<GifResolution> = listOf(),
    @SerializedName("source")
    var source: GifSource = GifSource()
)

data class Mp4(
    @SerializedName("resolutions")
    var resolutions: List<Mp4Resolution> = listOf(),
    @SerializedName("source")
    var source: Mp4Source = Mp4Source()
)

data class GifResolution(
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class GifSource(
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class Mp4Resolution(
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class Mp4Source(
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("url")
    var url: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class SecureMediaOembed(
    @SerializedName("author_name")
    var authorName: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("html")
    var html: String = "",
    @SerializedName("provider_name")
    var providerName: String = "",
    @SerializedName("provider_url")
    var providerUrl: String = "",
    @SerializedName("thumbnail_height")
    var thumbnailHeight: Int = 0,
    @SerializedName("thumbnail_url")
    var thumbnailUrl: String = "",
    @SerializedName("thumbnail_width")
    var thumbnailWidth: Int = 0,
    @SerializedName("title")
    var title: String = "",
    @SerializedName("type")
    var type: String = "",
    @SerializedName("version")
    var version: String = "",
    @SerializedName("width")
    var width: Int = 0
)

data class SecureMediaRedditVideo(
    @SerializedName("bitrate_kbps")
    var bitrateKbps: Int = 0,
    @SerializedName("dash_url")
    var dashUrl: String = "",
    @SerializedName("duration")
    var duration: Int = 0,
    @SerializedName("fallback_url")
    var fallbackUrl: String = "",
    @SerializedName("height")
    var height: Int = 0,
    @SerializedName("hls_url")
    var hlsUrl: String = "",
    @SerializedName("is_gif")
    var isGif: Boolean = false,
    @SerializedName("scrubber_media_url")
    var scrubberMediaUrl: String = "",
    @SerializedName("transcoding_status")
    var transcodingStatus: String = "",
    @SerializedName("width")
    var width: Int = 0
)