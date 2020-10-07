package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    private LocalDateTime localDateTime;
    private int num;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserPO user;
    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventPO rsEvent;
}
