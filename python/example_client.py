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


if __name__ == '__main__':
    scrape_using_proxy("http://icanhazip.com/")
