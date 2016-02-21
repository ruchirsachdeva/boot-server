package com.myapp.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserStats {
    private final long micropostCnt;
    private final long followingCnt;
    private final long followerCnt;
    private final boolean isFollowedByMe;
    private final boolean isMyself;

    @QueryProjection
    public UserStats(long micropostCnt, long followingCnt, long followerCnt, boolean isFollowedByMe, boolean isMyself) {
        this.micropostCnt = micropostCnt;
        this.followingCnt = followingCnt;
        this.followerCnt = followerCnt;
        this.isFollowedByMe = isFollowedByMe;
        this.isMyself = isMyself;
    }
}
