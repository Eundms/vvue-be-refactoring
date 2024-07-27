package com.exciting.vvue.place.model;

import com.exciting.vvue.place.model.dto.RecommendPlaceResDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NamedNativeQuery(
        name = "find_recommend_place_res_dto",
        query = "select p.*, " +
                "case when ps.avg_rating * 100 % 100 = 0 then FORMAT(ps.avg_rating,1) else FORMAT(ps.avg_rating, 2) end as avg_rating, " +
                "ps.visit_count, " +
                        "ST_Distance_Sphere(POINT(:lng, :lat), POINT(p.x, p.y)) as distance, " +
                        "case when fp.id is not null then true else false end as favorite " +
                        "from place_stats ps left join place p on ps.id = p.id " +
                        "left join favorite_place fp on p.id = fp.place_id and fp.user_id = :userId " +
                        "where ST_Distance_Sphere(POINT(:lng, :lat), POINT(p.x, p.y)) < :distance " +
                        "and (ps.avg_rating < :rateCursor " +
                        "or (ps.avg_rating = :rateCursor and p.id > :idCursor)) " +
                        "order by ps.avg_rating desc limit :size",
        resultSetMapping = "recommend_place_res_dto"
)
@SqlResultSetMapping(
        name = "recommend_place_res_dto",
        classes = @ConstructorResult(
                targetClass = RecommendPlaceResDto.class,
                columns = {
                        @ColumnResult(name = "id", type = String.class),
                        @ColumnResult(name = "address_name", type = String.class),
                        @ColumnResult(name = "category_group_code", type = String.class),
                        @ColumnResult(name = "category_group_name", type = String.class),
                        @ColumnResult(name = "category_name", type = String.class),
                        @ColumnResult(name = "phone", type = String.class),
                        @ColumnResult(name = "place_name", type = String.class),
                        @ColumnResult(name = "place_url", type = String.class),
                        @ColumnResult(name = "road_address_name", type = String.class),
                        @ColumnResult(name = "x", type = String.class),
                        @ColumnResult(name = "y", type = String.class),
                        @ColumnResult(name = "avg_rating", type = String.class),
                        @ColumnResult(name = "visit_count", type = String.class),
                        @ColumnResult(name = "distance", type = String.class),
                        @ColumnResult(name = "favorite", type = Boolean.class)
                }
        )
)
@Getter
@NoArgsConstructor
public class PlaceStats {
    @Id
    private Long id;
    private double avgRating;
    private long visitCount;

    @Builder
    public PlaceStats(Long id, double avgRating, long visitCount){
        this.id = id;
        this.avgRating = avgRating;
        this.visitCount = visitCount;
    }
}