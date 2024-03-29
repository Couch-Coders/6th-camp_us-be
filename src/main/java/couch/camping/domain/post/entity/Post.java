package couch.camping.domain.post.entity;

import couch.camping.domain.comment.entity.Comment;
import couch.camping.domain.member.entity.Member;
import couch.camping.domain.notification.entity.Notification;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
@EqualsAndHashCode
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImageList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PostLike> postLikeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Notification> notificationList = new ArrayList<>();

    private String title;

    @Lob
    private String content;

    private String postType;

    private int likeCnt;

    private int commentCnt;

    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    public void editPost(String title, String content, String hashTag) {
        this.title = title;
        this.content = content;
        this.postType = hashTag;
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void increaseLikeCnt() {
        this.likeCnt = likeCnt+1;
    }

    public void decreaseLikeCnt() {
        this.likeCnt = likeCnt - 1;
    }
}
