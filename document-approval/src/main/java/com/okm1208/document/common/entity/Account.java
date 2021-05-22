package com.okm1208.document.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
@Table(name="account")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNo;

    @Column(length = 30, nullable = false, unique = true)
    private String accountId;

    @Column(length = 200, nullable = false)
    private String password;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Approval> approvalList = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Document> regDocumentList = new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="account_authorities" ,joinColumns = @JoinColumn(name = "accountNo") )
    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private List<AccountAuthority> roles;

    public enum AccountAuthority implements GrantedAuthority {
        ROLE_ADMIN, ROLE_USER;

        @Override
        public String getAuthority() {
            return this.name();
        }

        @Override
        public String toString(){
            return this.name();
        }

        public static AccountAuthority findByName(String name){
            for(AccountAuthority v : values()){
                if( v.name().equals(name)){
                    return v;
                }
            }
            return null;
        }
    }

}
