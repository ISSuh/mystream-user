package mystream.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "subscribed_channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SubscribedChannel extends ModifyTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "subscribed_channel_id")
  private Long id;

  @Column(name = "channel_id", nullable = false)
  private Long channelId;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "subscribed_at", nullable = false)
  LocalDateTime subscribedAt;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "expired_at", nullable = false)
  LocalDateTime expiredAt;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "update_subscribed_at")
  LocalDateTime updateSubscribedAt;
}
