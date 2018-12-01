# My Homelab

This page describe my home lab setup.

# In photos

Arriving soon…


# In details

## Case

* 1 x Cablematic - Rack serveur 19'' 12U ([on Amazon FR](https://www.amazon.fr/gp/product/B00IHWOZOA/))
* 1 x Perel 37331 Bloc 8 Prises pour montage Rack 19" ([on Amazon FR](https://www.amazon.fr/gp/product/B00GMPRIAW/))
* 4 x Digitus étagère 19" 1 U montage fixe prof 250 mm Noir ([on Amazon FR](https://www.amazon.fr/gp/product/B002KTE870/))
* 1 x RackMatic - Kit de Ventilation Thermostat Serveur Rack 19 2 Ventilateurs de 120mm ([on Amazon FR](https://www.amazon.fr/gp/product/B01M1V0HRA/))
* 1 x câble Gestion 19 Universel Panneau 1U Avec Brosse Pour Données Schrank ([on Amazon FR](https://www.amazon.fr/gp/product/B004HTHL20/))

## ISP

Orange Fiber (with ONT).


## Router [ERPro-8](https://www.ubnt.com/edgemax/edgerouter-pro/)

* [On Amazon FR](https://www.amazon.fr/gp/product/B00IA5J8M8/)
* [My config](src/config.boot), with the help of these articles:
    * [How to replace your ISP box with Ubiquiti Edgemax](https://lafibre.info/remplacer-livebox/en-cours-remplacer-sa-livebox-par-un-routeur-ubiquiti-edgemax/msg428992/#msg428992)
    * [Edgerouter and Chromecast](https://www.cron.dk/edgerouter-and-chromecast/)

* 5 VLANs ():
    1. `Management`: `192.168.1.X`, no external or internal access.
    1. `Home`: `192.168.2.X`, can access to WAN/NAT, Guest and Disconnected.
    1. `Guest`: `192.168.3.X`, can access to WAN/NAT.
    1. `Disconnected`: `192.168.4.X`, no external or internal access, but stay accessible from outside.
    1. `Work`: `192.168.5.X`, can access to WAN/NAT.


## Wifi

1 x [Unifi AP AC Pro](https://www.ubnt.com/unifi/unifi-ap-ac-pro/)

Connected to `Management` (untagged), `Home` (tagged) and `Guest` (tagged) on one switch port.


## Switch

1 x [TP-Link TL-SG1024DE](https://www.tp-link.com/us/products/details/TL-SG1024DE.html), got cables connected to `Management`, `Home`, `Guest`, `Disconnected`. Default VLAN port is `Management`.


## NAS

1 x Synology DS216+II, Connected to `Disconnected` via one switch port.


## Printer

1 x HP Officejet Pro 8600 N911g, Connected to `Disconnected` via one switch port.
