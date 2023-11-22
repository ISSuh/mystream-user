package mystream.user.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.google.common.base.Preconditions;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "followed_channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FollowedChannel extends ModifyTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "followed_channel_id")
  private Long id;

  @Column(name = "channel_id", nullable = false)
  private Long channelId;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "followed_at")
  private LocalDateTime followedAt;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "unfollowed_at")
  private LocalDateTime unfollowedAt;

  @Enumerated
  @Column(name = "follow_status")
  @ColumnDefault("'UNFOLLOW'")
  private FollowStatus followStatus;

  public FollowedChannel(Long channelId, User user) {
    this(null, channelId, user, null, null, FollowStatus.UNFOLLOW);
  }

  public FollowedChannel(Long id, Long channelId, User user, LocalDateTime followedAt, LocalDateTime unfollowedAt,
      FollowStatus followStatus) {
    Preconditions.checkArgument(channelId != null, "channelId must be not null");

    this.id = id;
    this.channelId = channelId;
    this.user = user;
    this.followedAt = followedAt;
    this.unfollowedAt = unfollowedAt;
    this.followStatus = followStatus;
  }

  public void updateFollowStatus(FollowStatus status) {
    this.followStatus = status;

    if (status == FollowStatus.FOLLOW) {
      this.user.follwedChannel(this);
      this.followedAt = LocalDateTime.now();
    } else {
      this.user.unfollwedChannel(this);
      this.unfollowedAt = LocalDateTime.now();
    }
  }

}
