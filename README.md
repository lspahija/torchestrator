# Torchestrator

Torchestrator can spin up Tor containers and expose ports for proxying HTTP requests via these Tor instances.

The IP address of the exit node of each Tor instance will vary. This is useful for IP address rotation.

###To run:
    
- `cd torchestrator/docker/torprivoxydocker`
- `docker build -t torprivoxy:1.0 .`
- set number of Tor containers you wish to spin up with config property `tor.containerQuantity` in `torchestrator/src/main/resources/application.properties` 
  (default is 10, but can be a much higher number depending on RAM)
- `./gradlew bootRun` (with JDK 15) or run in an IDE like IntelliJ IDEA
- get next proxy port with `GET localhost:8080/port`

##Examples

Scraping from Python using Torchestrator as a proxy provider:
```python
import requests

torchestrator_host = "http://localhost"
tochestrator_port = 8080
torchestrator_api_path = "port"


def fetch_proxy_port():
    response = requests.get(f'{torchestrator_host}:{tochestrator_port}/{torchestrator_api_path}')
    return response.text


def get_using_proxy(url, proxy):
    return requests.get(url, proxies={"http": proxy, "https": proxy})


def scrape_using_proxy(url):
    proxy_port = fetch_proxy_port()
    response = get_using_proxy(url, f'{torchestrator_host}:{proxy_port}')
    print(response.text)
  
    
scrape_with_proxy("http://icanhazip.com/")
```

Similarly, a sample Kotlin HTTP client proxying requests via Tor instances can be found in `torchestrator/src/main/kotlin/com/alealogic/torchestrator/client/ExampleClient.kt`
