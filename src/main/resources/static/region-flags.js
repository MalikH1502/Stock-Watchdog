const REGION_FLAGS = {
    "United States": "🇺🇸",
    "United Kingdom": "🇬🇧",
    "Frankfurt": "🇩🇪",
    "XETRA": "🇩🇪",
    "Canada": "🇨🇦",
    "Toronto Venture": "🇨🇦",
    "India/Bombay": "🇮🇳",
    "China": "🇨🇳",
    "Shanghai": "🇨🇳",
    "Shenzhen": "🇨🇳",
    "Hong Kong": "🇭🇰",
    "Paris": "🇫🇷",
    "Amsterdam": "🇳🇱",
    "Brussels": "🇧🇪",
    "Milan": "🇮🇹",
    "Sao Paolo": "🇧🇷",
    "Stockholm": "🇸🇪",
    "Copenhagen": "🇩🇰",
    "Helsinki": "🇫🇮",
    "Ireland": "🇮🇪",
    "Israel": "🇮🇱",
    "Estonia": "🇪🇪"
};

function getRegionFlag(region) {
    return REGION_FLAGS[region] || "";
}