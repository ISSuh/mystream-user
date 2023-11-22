package mystream.user.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

  @Embedded
  @Column(name = "email")
  private Email email;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  // private Long channelId;

  @OneToMany(mappedBy = "user")
  Map<Long, FollowedChannel> followingChannels = new HashMap<>();

  @OneToMany(mappedBy = "user")
  List<SubscribedChannel> subscribedChannels = new ArrayList<>();

  public void follwedChannel(FollowedChannel channel) {
    this.followingChannels.put(channel.getChannelId(), channel);
  }

  public void unfollwedChannel(FollowedChannel channel) {
    this.followingChannels.put(channel.getChannelId(), channel);
  }

  public void addSubscribedChannel(SubscribedChannel channel) {
    this.subscribedChannels.add(channel);
  }
}
