package couch.camping.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String imgUrl;

    private String name;

    private String nickname;
}
