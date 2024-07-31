package com.ilogistic.delivery_admin_backend.file.domain.entity

import javax.persistence.*

@Entity
class File(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        var path: String? = null,
        var originalFilename: String? = null,
        var filename: String? = null,

//        @JsonIgnore
//        @OneToOne(fetch = FetchType.LAZY)
//        @JoinColumn(name = "file_board_id")
//        var fileBoard: FileBoard? = null
) {
}
