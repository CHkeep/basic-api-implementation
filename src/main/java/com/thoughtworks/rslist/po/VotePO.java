package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "vote")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotePO {
    @Id
    @GeneratedValue
    private int id;

    @JsonEnumDefaultValue
    private LocalDateTime localDateTime;
    private int num;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserPO user;
    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventPO rsEvent;
}
