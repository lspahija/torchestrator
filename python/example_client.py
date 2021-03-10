import requests

torchestrator_host = "http://localhost"
tochestrator_port = 8080
torchestrator_api_path = "port"


def scrape_with_proxy(url):
    proxy_port = fetch_proxy_port()
    response = get_using_proxy(url, f'{torchestrator_host}:{proxy_port}')
    print(response.text)


def get_using_proxy(url, proxy):
    res = requests.get(url, proxies={"http": proxy, "https": proxy})
    res.raise_for_status()
    return res


def fetch_proxy_port():
    res = requests.get(f'{torchestrator_host}:{tochestrator_port}/{torchestrator_api_path}')
    res.raise_for_status()
    return res.text


if __name__ == '__main__':
    scrape_with_proxy("http://icanhazip.com/")
