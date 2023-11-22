package mystream.user.entity;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends ModifyTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(name = "channel_id", unique = true)
  private Long channelId;

  @Embedded
  @Column(name = "email", nullable = false, unique = true)
  private Email email;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "password", nullable = false)
  private String password;

  @OneToMany(mappedBy = "user")
  Map<Long, FollowedChannel> followingChannels = new HashMap<>();

  @OneToMany(mappedBy = "user")
  Map<Long, SubscribedChannel> subscribedChannels = new HashMap<>();

  public User(Email email, String username, String password) {
    this(null, null, email, username, password, null, null);
  }

  public User(Long id, Long channelId, Email email, String username, String password,
      Map<Long, FollowedChannel> followingChannels, Map<Long, SubscribedChannel> subscribedChannels) {
    this.id = id;
    this.channelId = channelId;
    this.email = email;
    this.username = username;
    this.password = password;

    if (followingChannels != null) {
      this.followingChannels = followingChannels;
    }

    if (subscribedChannels != null) {
      this.subscribedChannels = subscribedChannels;
    }
  }

  public void updateUserName(String username) {
    this.username = username;
  }

  public void updateEmail(Email email) {
    this.email = email;
  }

  public void follwedChannel(FollowedChannel channel) {
    this.followingChannels.put(channel.getChannelId(), channel);
  }

  public void unfollwedChannel(FollowedChannel channel) {
    this.followingChannels.remove(channel.getChannelId());
  }

  public void subscribedChannel(SubscribedChannel channel) {
    this.subscribedChannels.put(channel.getChannelId(), channel);
  }

  public void unSubscribedChannel(SubscribedChannel channel) {
    this.subscribedChannels.remove(channel.getChannelId());
  }

}
