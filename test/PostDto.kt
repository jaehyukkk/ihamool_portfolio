package com.ilogistic.delivery_admin_backend.test

class PostDto(
        var id: Long? = null,
        var subject: String,
        var content: String
) {
    fun toEntity() : Post {
        return Post(
                subject = subject,
                content = content
        )
    }

    companion object{
        fun of(post : Post) : PostDto {
            return PostDto(
                    id = post.id,
                    subject = post.subject,
                    content = post.content
            )
        }
    }
}
