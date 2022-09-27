## Lokifier
Bring Loki log observability to Minecraft 

## TODO
- [x] Stream all
- [x] Basic auth support
- [ ] Other platforms (Velocity, Sponge)

~~Extensive filtering of log records to apply in different streams with different labels~~ _(Will not be implemented check UPD below)_

## Versioning
At the moment bukkit version is built against 1.12.2 Paper. If modern versions does not work as expected please make an issue.


### UPD

Filtering of log records should be performed by promtail.
It can mock loki api and reicive logs produced by this plugin.
promtail has sophisticated [pipelines functionality](https://grafana.com/docs/loki/latest/clients/promtail/pipelines/) 
which allows extensive filtering: relabling, data extraction and other things. If more people request this 
I will try to implement something similar but at the time is not really worth the effort
