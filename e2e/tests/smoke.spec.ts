import { test, expect } from '@playwright/test';

test.describe('Cosmic Catalog Smoke Tests', () => {
  test('home page loads successfully', async ({ page }) => {
    await page.goto('/');
    
    // Check that the page has loaded and contains expected content
    await expect(page).toHaveTitle(/Cosmic/i);
    
    // Check for main heading or content
    const heading = page.locator('h1, h2').first();
    await expect(heading).toBeVisible();
    const headingText = await heading.textContent();
    expect(headingText?.toLowerCase()).toContain('cosmic');
  });

  test('API: import sample data and fetch featured observations', async ({ request }) => {
    // Import sample data
    const importResponse = await request.post('/api/import/sample');
    expect(importResponse.ok()).toBeTruthy();
    expect(importResponse.status()).toBe(200);
    
    // Get the import summary
    const importData = await importResponse.json();
    expect(importData).toHaveProperty('totalProcessed');
    expect(importData).toHaveProperty('imported');
    
    // Small delay to ensure data is processed
    await new Promise(resolve => setTimeout(resolve, 500));
    
    // Fetch featured observations
    const featuredResponse = await request.get('/api/featured?limit=2');
    expect(featuredResponse.ok()).toBeTruthy();
    expect(featuredResponse.status()).toBe(200);
    
    const featuredData = await featuredResponse.json();
    expect(featuredData).toBeInstanceOf(Array);
    expect(featuredData.length).toBeLessThanOrEqual(2);
    
    // Verify structure of featured observations if any exist
    if (featuredData.length > 0) {
      expect(featuredData[0]).toHaveProperty('id');
      expect(featuredData[0]).toHaveProperty('telescope');
      expect(featuredData[0]).toHaveProperty('targetName');
      expect(featuredData[0]).toHaveProperty('score');
    }
  });

  test('API: observations endpoint with pagination', async ({ request }) => {
    // Test the observations endpoint with pagination
    const response = await request.get('/api/observations?size=1');
    expect(response.ok()).toBeTruthy();
    expect(response.status()).toBe(200);
    
    const data = await response.json();
    expect(data).toHaveProperty('content');
    expect(data.content).toBeInstanceOf(Array);
    expect(data.content.length).toBeLessThanOrEqual(1);
    
    // Check pagination metadata
    expect(data).toHaveProperty('pageable');
    expect(data).toHaveProperty('totalElements');
    expect(data).toHaveProperty('totalPages');
    expect(data).toHaveProperty('size');
    expect(data.size).toBe(1);
  });
});