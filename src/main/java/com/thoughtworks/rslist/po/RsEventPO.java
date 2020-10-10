package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rsEvent")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsEventPO {
    @Id
    @GeneratedValue(generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    @Column(name = "name")
    private String eventName;
    private String keyWords;
    private  int voteNum = 0;
    @ManyToOne
    private UserPO userPO;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "rsEvent")
    private List<VotePO> votePOS;

}


