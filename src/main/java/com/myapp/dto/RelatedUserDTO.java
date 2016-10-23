package com.myapp.dto;

import com.myapp.Utils;
import com.myapp.domain.Relationship;
import com.myapp.domain.User;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RelatedUserDTO {

    private final long id;
    private final String email;
    private final String name;
    private final String avatarHash;
    private final UserStats userStats;
    private final Boolean isMyself;
    private final Relationship relationship;

    public static RelatedUserDTO newInstance(User user, Relationship relationship, UserStats userStats, Boolean isMyself) {
        final String avatarHash = Utils.md5(user.getUsername());

        return RelatedUserDTO.builder()
                .id(user.getId())
                .email(user.getUsername())
                .name(user.getName())
                .avatarHash(avatarHash)
                .isMyself(isMyself)
                .userStats(userStats)
                .relationship(relationship)
                .build();
    }
}
