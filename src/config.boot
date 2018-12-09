firewall {
    all-ping enable
    broadcast-ping disable
    group {
        network-group BOGON {
            network 0.0.0.0/8
            network 10.0.0.0/8
            network 100.64.0.0/10
            network 127.0.0.0/8
            network 169.254.0.0/16
            network 172.16.0.0/12
            network 192.0.0.0/24
            network 192.0.2.0/24
            network 192.168.0.0/16
            network 192.18.0.0/15
            network 198.51.100.0/24
            network 203.0.113.0/24
            network 224.0.0.0/4
            network 240.0.0.0/4
        }
        network-group LAN {
            description "LAN Networks"
            network 192.168.0.0/16
            network 172.16.0.0/12
            network 10.0.0.0/8
        }
    }
    ipv6-receive-redirects disable
    ipv6-src-route disable
    ip-src-route disable
    log-martians enable
    name DISCONNECTED_IN {
        default-action drop
        enable-default-log
        rule 10 {
            action accept
            description "Allow related and established"
            log disable
            protocol all
            state {
                established enable
                invalid disable
                new disable
                related enable
            }
        }
        rule 20 {
            action drop
            description "Drop invalid and new"
            log disable
            protocol all
            state {
                established disable
                invalid enable
                new enable
                related disable
            }
        }
    }
    name DISCONNECTED_LOCAL {
        default-action drop
        enable-default-log
        rule 10 {
            action accept
            description "Allow DHCP"
            destination {
                port 67
            }
            log disable
            protocol udp
        }
        rule 20 {
            action accept
            description "Allow NTP"
            destination {
                port 123
            }
            log disable
            protocol udp
        }
        rule 30 {
            action accept
            description "Allow DNS"
            destination {
                port 53
            }
            log disable
            protocol tcp_udp
        }
    }
    name GUEST_IN {
        default-action accept
        enable-default-log
        rule 10 {
            action drop
            description "Drop LAN network group"
            destination {
                group {
                    network-group LAN
                }
            }
            log disable
            protocol all
            state {
                invalid enable
                new enable
            }
        }
    }
    name GUEST_LOCAL {
        default-action drop
        enable-default-log
        rule 10 {
            action accept
            description "Accept DHCP"
            destination {
                port 67
            }
            protocol udp
        }
        rule 20 {
            action accept
            description "Allow mDNS"
            destination {
                port 5353
            }
            protocol udp
        }
    }
    name HOME_IN {
        default-action accept
        enable-default-log
        rule 10 {
            action drop
            description "Drop Work LAN"
            destination {
                group {
                    address-group ADDRv4_eth5
                }
            }
            log disable
            protocol all
        }
    }
    name HOME_LOCAL {
        default-action drop
        enable-default-log
        rule 10 {
            action accept
            description "Accept DHCP"
            destination {
                port 67
            }
            protocol udp
        }
        rule 20 {
            action accept
            description "Accept DNS"
            destination {
                port 53
            }
            protocol tcp_udp
        }
        rule 30 {
            action accept
            description "Accept mDNS"
            destination {
                port 5353
            }
            protocol udp
        }
    }
    name MANAGEMENT_IN {
        default-action drop
        enable-default-log
    }
    name WAN_IN {
        default-action drop
        enable-default-log
        rule 10 {
            action accept
            description "Allow established connections"
            log disable
            protocol all
            state {
                established enable
                invalid disable
                new disable
                related enable
            }
        }
        rule 20 {
            action drop
            description "Drop invalid state"
            log disable
            protocol all
            state {
                established disable
                invalid enable
                new disable
                related disable
            }
        }
        rule 30 {
            action drop
            description "Drop BOGON source"
            log disable
            protocol all
            source {
                group {
                    network-group BOGON
                }
            }
        }
    }
    name WAN_LOCAL {
        default-action drop
        enable-default-log
        rule 10 {
            action accept
            description "Allow established connections"
            log disable
            protocol all
            state {
                established enable
                related enable
            }
        }
        rule 20 {
            action drop
            description "Drop invalid state"
            log disable
            protocol all
            state {
                invalid enable
            }
        }
    }
    name WORK_IN {
        default-action accept
        enable-default-log
        rule 10 {
            action drop
            description "Drop LAN network group"
            destination {
                group {
                    network-group LAN
                }
            }
            log disable
            protocol all
        }
    }
    name WORK_LOCAL {
        default-action drop
        enable-default-log
        rule 10 {
            action accept
            description "Accept DHCP"
            destination {
                port 67
            }
            protocol udp
        }
    }
    receive-redirects disable
    send-redirects enable
    source-validation disable
    syn-cookies enable
}
interfaces {
    ethernet eth0 {
        description WAN
        duplex auto
        speed auto
        vif 832 {
            address dhcp
            description FTTH
            dhcp-options {
                client-option "send vendor-class-identifier &quot;sagem&quot;;"
                client-option "send user-class &quot;\053FSVDSL_livebox.Internet.softathome.Livebox4&quot;;"
                client-option "request subnet-mask, routers, domain-name-servers, domain-name, broadcast-address, dhcp-lease-time, dhcp-renewal-time, dhcp-rebinding-time, rfc3118-auth;"
                client-option "send rfc3118-auth 00:00:00:00:00:00:00:00:00:00:00:1a:09:00:00:05:58:01:03:41:01:0d:xx:xx:xx:xx:xx:xx:xx:xx:xx:xx:xx;"
                default-route update
                default-route-distance 210
                global-option "option rfc3118-auth code 90 = string;"
                name-server update
            }
            egress-qos "0:0 1:0 2:0 3:0 4:0 5:0 6:6 7:0"
            firewall {
                in {
                    name WAN_IN
                }
                local {
                    name WAN_LOCAL
                }
            }
        }
    }
    ethernet eth1 {
        address 192.168.1.1/24
        description Management
        duplex auto
        firewall {
            in {
                name MANAGEMENT_IN
            }
        }
        speed auto
    }
    ethernet eth2 {
        address 192.168.2.1/24
        description Home
        duplex auto
        firewall {
            in {
                name HOME_IN
            }
            local {
                name HOME_LOCAL
            }
        }
        speed auto
    }
    ethernet eth3 {
        address 192.168.3.1/24
        description Guest
        duplex auto
        firewall {
            in {
                name GUEST_IN
            }
            local {
                name GUEST_LOCAL
            }
        }
        speed auto
    }
    ethernet eth4 {
        address 192.168.4.1/24
        description Disconnected
        duplex auto
        firewall {
            in {
                name DISCONNECTED_IN
            }
            local {
                name DISCONNECTED_LOCAL
            }
        }
        speed auto
    }
    ethernet eth5 {
        address 192.168.5.1/24
        description Work
        duplex auto
        firewall {
            in {
                name WORK_IN
            }
            local {
                name WORK_LOCAL
            }
        }
        speed auto
    }
    ethernet eth6 {
        duplex auto
        speed auto
    }
    ethernet eth7 {
        duplex auto
        speed auto
    }
    loopback lo {
    }
}
service {
    dhcp-server {
        disabled false
        hostfile-update disable
        shared-network-name disconnected {
            authoritative enable
            subnet 192.168.4.0/24 {
                default-router 192.168.4.1
                dns-server 192.168.4.1
                domain-name home.local
                lease 86400
                ntp-server 192.168.4.1
                start 192.168.4.10 {
                    stop 192.168.4.100
                }
                static-mapping printer {
                    ip-address 192.168.4.11
                    mac-address ec:9a:74:90:56:a3
                }
            }
        }
        shared-network-name guest {
            authoritative enable
            subnet 192.168.3.0/24 {
                default-router 192.168.3.1
                dns-server 208.67.222.222
                dns-server 208.67.220.220
                lease 86400
                start 192.168.3.10 {
                    stop 192.168.3.100
                }
            }
        }
        shared-network-name home {
            authoritative enable
            subnet 192.168.2.0/24 {
                default-router 192.168.2.1
                dns-server 192.168.2.1
                domain-name home.local
                lease 86400
                start 192.168.2.10 {
                    stop 192.168.2.100
                }
            }
        }
        shared-network-name management {
            authoritative enable
            subnet 192.168.1.0/24 {
                default-router 192.168.1.1
                lease 86400
                ntp-server 192.168.1.1
                start 192.168.1.10 {
                    stop 192.168.1.100
                }
            }
        }
        shared-network-name work {
            authoritative enable
            subnet 192.168.5.0/24 {
                default-router 192.168.5.1
                dns-server 208.67.222.222
                dns-server 208.67.220.220
                lease 86400
                start 192.168.5.10 {
                    stop 192.168.5.100
                }
            }
        }
        static-arp disable
        use-dnsmasq enable
    }
    dns {
        forwarding {
            cache-size 400
            listen-on eth2
            listen-on eth4
            name-server X.X.X.X
        }
    }
    gui {
        http-port 80
        https-port 443
        listen-address 192.168.1.1
        older-ciphers disable
    }
    mdns {
        repeater {
            interface eth2
            interface eth3
        }
    }
    nat {
        rule 5000 {
            description "Masquerade for WAN"
            log disable
            outbound-interface eth0.832
            protocol all
            type masquerade
        }
    }
    ssh {
        disable-password-authentication
        listen-address 192.168.1.1
        port 22
        protocol-version v2
    }
}
system {
    domain-name home.local
    host-name edgerouter
    login {
        user admin {
            authentication {
                encrypted-password …
                plaintext-password ""
                public-keys my_infra {
                    key …
                    type ssh-rsa
                }
            }
            level admin
        }
    }
    name-server 127.0.0.1
    ntp {
        server 0.pool.ntp.org {
        }
        server 1.pool.ntp.org {
        }
        server 2.pool.ntp.org {
        }
        server 3.pool.ntp.org {
        }
    }
    offload {
        hwnat disable
        ipv4 {
            forwarding enable
            gre enable
            pppoe enable
            vlan enable
        }
    }
    syslog {
        global {
            facility all {
                level notice
            }
            facility protocols {
                level debug
            }
        }
    }
    time-zone Europe/Paris
    traffic-analysis {
        dpi disable
        export disable
    }
}


/* Warning: Do not remove the following line. */
/* === vyatta-config-version: "config-management@1:conntrack@1:cron@1:dhcp-relay@1:dhcp-server@4:firewall@5:ipsec@5:nat@3:qos@1:quagga@2:suspend@1:system@4:ubnt-pptp@1:ubnt-udapi-server@1:ubnt-unms@1:ubnt-util@1:vrrp@1:webgui@1:webproxy@1:zone-policy@1" === */
/* Release version: v1.10.8.5142441.181120.1647 */
