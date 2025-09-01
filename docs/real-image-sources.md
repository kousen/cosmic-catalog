# Real Telescope Image Sources

## NASA Image Libraries with Direct URLs

### 1. NASA Image and Video Library
- API: https://images-api.nasa.gov/
- Example search: https://images-api.nasa.gov/search?q=jwst
- Returns JSON with actual image URLs

### 2. Hubble Legacy Archive
- Public images: https://hla.stsci.edu/
- Direct image URLs available

### 3. JWST Public Gallery
- Gallery: https://webbtelescope.org/images
- High-res downloads available

### 4. Specific Real Image URLs You Can Use

Here are some actual working image URLs you could update in the JSON files:

```json
{
  "targetName": "Carina Nebula",
  "imageUrl": "https://stsci-opo.org/STScI-01G7ETPF7DVBJAC42JR5N6EQRH.png"
}

{
  "targetName": "Stephan's Quintet", 
  "imageUrl": "https://stsci-opo.org/STScI-01G7ETQ0K9CJHFB7ZEJY2Q9XQM.png"
}

{
  "targetName": "Southern Ring Nebula",
  "imageUrl": "https://stsci-opo.org/STScI-01G7ETR89P8201X19EKCXPAMXG.png"
}

{
  "targetName": "SMACS 0723",
  "imageUrl": "https://stsci-opo.org/STScI-01G7DCWB7137VJZ2PBJT023W1Z.png"
}
```

## How to Update Your Data

1. Edit `src/main/resources/data/realistic_jwst.json`
2. Replace the fake URLs with real ones from above
3. Or use NASA's Image API to dynamically fetch images

## Example: Using NASA Image API

```javascript
// Fetch real image for a target
async function fetchNASAImage(targetName) {
    const response = await fetch(`https://images-api.nasa.gov/search?q=${targetName}&media_type=image`);
    const data = await response.json();
    if (data.collection.items.length > 0) {
        // Get the first image result
        const imageUrl = data.collection.items[0].links[0].href;
        return imageUrl;
    }
    return null;
}
```

## Quick Fix: Update One Observation

To test with a real image, update one observation in your database:

```sql
UPDATE observation 
SET image_url = 'https://stsci-opo.org/STScI-01G7ETPF7DVBJAC42JR5N6EQRH.png'
WHERE target_name = 'Carina Nebula';
```

Then update the template to show real images when available:

```html
<img th:if="${obs.imageUrl != null and obs.imageUrl.startsWith('http')}" 
     th:src="${obs.imageUrl}" 
     class="card-img-top" 
     th:alt="${obs.targetName}"/>
```