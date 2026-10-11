package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RegionalPhotoManifest
import com.example.data.TimizarDatabase
import com.example.model.Destination
import com.example.model.LocalProduct
import com.example.model.ServiceItem
import com.example.model.CommunityPost
import com.example.state.ActiveScreen
import com.example.state.AppUiState
import com.example.state.TimizarViewModel
import com.example.ui.components.MoroccanArtwork
import com.example.ui.theme.*

/**
 * Clean, minimal, photography-first Home Screen for SIA Drâa-Tafilalet.
 * Philosophy: LESS TEXT. MORE PHOTOGRAPHY. BETTER EXPERIENCE.
 */
@Composable
fun HomeScreen(
    uiState: AppUiState,
    viewModel: TimizarViewModel,
    modifier: Modifier = Modifier
) {
    val destinations = remember { TimizarDatabase.destinations }
    val activities = remember { TimizarDatabase.services.filter { it.category.label == "Activities" || it.category.label == "Cooperatives" } }
    val marketplaceProducts = uiState.marketplaceProducts
    val communityPosts = uiState.communityPosts

    LazyColumn(
        modifier = modifier
            .testTag("home_screen")
            .fillMaxSize()
            .background(WarmIvory),
        verticalArrangement = Arrangement.spacedBy(28.dp),
        contentPadding = PaddingValues(bottom = 36.dp)
    ) {
        // 1. Minimal Brand Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Terracotta),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "S",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "SIA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Charcoal,
                                fontSize = 18.sp,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "DRÂA-TAFILALET",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MutedText,
                            letterSpacing = 1.5.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { viewModel.navigateTo(ActiveScreen.COMMUNITY) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CardWhite)
                            .testTag("home_top_community_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = "Community",
                            tint = Charcoal,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.navigateTo(ActiveScreen.BOOKINGS) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CardWhite)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Charcoal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // 2. Minimal Search Bar
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo(ActiveScreen.SEARCH) }
                        .testTag("home_search_bar"),
                    shape = RoundedCornerShape(16.dp),
                    color = CardWhite,
                    border = BorderStroke(1.dp, BorderColor)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MutedText,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Search places, crafts, experiences...",
                            color = MutedText,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // 2b. Quick Navigation Bar (Direct access to Community, Explore, Market, Plan, Transport)
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val quickShortcuts = listOf(
                    Triple(ActiveScreen.COMMUNITY, "Community", Icons.Default.Groups),
                    Triple(ActiveScreen.EXPLORE, "Explore", Icons.Default.Explore),
                    Triple(ActiveScreen.EXPERIENCES, "Activities", Icons.Default.LocalActivity),
                    Triple(ActiveScreen.LOCAL_MARKET, "Market", Icons.Default.Storefront),
                    Triple(ActiveScreen.TRIP_PLANNER, "Plan Trip", Icons.Default.DateRange),
                    Triple(ActiveScreen.TRANSPORT_HUB, "Transport", Icons.Default.DirectionsBus)
                )
                items(quickShortcuts) { (screen, label, icon) ->
                    val isCommunity = screen == ActiveScreen.COMMUNITY
                    Surface(
                        modifier = Modifier
                            .clickable { viewModel.navigateTo(screen) }
                            .testTag(if (isCommunity) "home_quick_community_button" else "home_shortcut_${label.lowercase()}"),
                        shape = RoundedCornerShape(20.dp),
                        color = if (isCommunity) Terracotta.copy(alpha = 0.12f) else CardWhite,
                        border = BorderStroke(1.dp, if (isCommunity) Terracotta else BorderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isCommunity) Terracotta else Charcoal,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isCommunity) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCommunity) Terracotta else Charcoal
                            )
                        }
                    }
                }
            }
        }

        // 3. Beautiful Full-Width Regional Hero
        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { viewModel.navigateTo(ActiveScreen.EXPLORE) }
            ) {
                MoroccanArtwork(
                    placeId = "merzouga",
                    imageUrl = RegionalPhotoManifest.DESTINATION_MERZOUGA_DUNES,
                    contentDescription = "Erg Chebbi Dunes",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Elegant subtle vignette gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.72f)),
                                startY = 80f
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "Discover Drâa-Tafilalet",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ancient ksour, desert dunes & verdant oases",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Terracotta,
                        modifier = Modifier.clickable { viewModel.navigateTo(ActiveScreen.EXPLORE) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Explore",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. Featured Discover (Destination Cards)
        item {
            Column {
                SectionHeader(
                    title = "Featured Destinations",
                    actionLabel = "See all",
                    onActionClick = { viewModel.navigateTo(ActiveScreen.EXPLORE) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(destinations) { destination ->
                        CleanDestinationCard(
                            destination = destination,
                            onExploreClick = { viewModel.selectPlace(destination.id) }
                        )
                    }
                }
            }
        }

        // 5. Popular Activities (Activities Cards)
        item {
            Column {
                SectionHeader(
                    title = "Popular Activities",
                    actionLabel = "See all",
                    onActionClick = { viewModel.navigateTo(ActiveScreen.EXPERIENCES) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(activities) { activity ->
                        CleanActivityCard(
                            activity = activity,
                            onCardClick = { viewModel.selectService(activity.id) }
                        )
                    }
                }
            }
        }

        // 6. Local Marketplace (Marketplace Cards)
        item {
            Column {
                SectionHeader(
                    title = "Artisan Marketplace",
                    actionLabel = "See all",
                    onActionClick = { viewModel.navigateTo(ActiveScreen.LOCAL_MARKET) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(marketplaceProducts) { product ->
                        val isFav = uiState.favoriteProductIds.contains(product.id)
                        CleanMarketplaceCard(
                            product = product,
                            isFavorite = isFav,
                            onProductClick = { viewModel.selectProduct(product.id) },
                            onFavoriteClick = { viewModel.toggleProductFavorite(product.id) }
                        )
                    }
                }
            }
        }

        // 7. Community Stories
        item {
            Column {
                SectionHeader(
                    title = "Community Stories",
                    actionLabel = "View all",
                    onActionClick = { viewModel.navigateTo(ActiveScreen.COMMUNITY) }
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(communityPosts) { post ->
                        CleanCommunityCard(
                            post = post,
                            onPostClick = { viewModel.navigateTo(ActiveScreen.COMMUNITY) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Button(
                        onClick = { viewModel.navigateTo(ActiveScreen.COMMUNITY) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_community_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TwilightIndigo)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Explore Community & Traveler Stories",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionLabel: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Charcoal,
                fontSize = 18.sp
            )
        )
        Text(
            text = actionLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Terracotta,
            modifier = Modifier.clickable { onActionClick() }
        )
    }
}

/**
 * Destination Card displaying ONLY:
 * - Beautiful destination image
 * - Destination name
 * - Location
 * - Explore action
 */
@Composable
fun CleanDestinationCard(
    destination: Destination,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(220.dp)
            .clickable { onExploreClick() }
            .testTag("destination_card_${destination.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                MoroccanArtwork(
                    placeId = destination.id,
                    imageUrl = destination.imageUrl,
                    contentDescription = destination.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = destination.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Charcoal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = destination.province,
                    fontSize = 12.sp,
                    color = MutedText,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Explore →",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Terracotta
                    )
                }
            }
        }
    }
}

/**
 * Activity Card displaying ONLY:
 * - Real activity photo
 * - Activity title
 * - Location
 * - Duration, if known
 */
@Composable
fun CleanActivityCard(
    activity: ServiceItem,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val place = TimizarDatabase.destinations.find { it.id == activity.placeId }
    val locationLabel = place?.name ?: "Drâa-Tafilalet"

    Card(
        modifier = modifier
            .width(230.dp)
            .clickable { onCardClick() }
            .testTag("activity_card_${activity.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                MoroccanArtwork(
                    placeId = activity.placeId,
                    imageUrl = activity.imageUrl,
                    contentDescription = activity.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = activity.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Charcoal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = locationLabel,
                        fontSize = 12.sp,
                        color = MutedText
                    )
                    if (activity.durationText.isNotBlank()) {
                        Text(
                            text = activity.durationText.replace("/", "").trim(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = OasisGreen
                        )
                    }
                }
            }
        }
    }
}

/**
 * Marketplace Card displaying ONLY:
 * - Product photo
 * - Product name
 * - Origin
 * - Verified price, when available
 * - Favorite icon
 */
@Composable
fun CleanMarketplaceCard(
    product: LocalProduct,
    isFavorite: Boolean,
    onProductClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(180.dp)
            .clickable { onProductClick() }
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                MoroccanArtwork(
                    placeId = product.placeId,
                    imageUrl = product.imageUrl,
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Favorite Icon
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.9f))
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) AlertRed else Charcoal,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Charcoal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = product.origin,
                    fontSize = 11.sp,
                    color = MutedText
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${product.priceMAD} MAD",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Terracotta
                )
            }
        }
    }
}

/**
 * Community Card displaying large photograph, readable caption, author & location.
 */
@Composable
fun CleanCommunityCard(
    post: CommunityPost,
    onPostClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(260.dp)
            .clickable { onPostClick() }
            .testTag("community_post_${post.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                MoroccanArtwork(
                    placeId = post.placeId,
                    imageUrl = post.imageUrl,
                    contentDescription = post.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = post.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Charcoal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = post.content,
                    fontSize = 12.sp,
                    color = MutedText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = post.authorName,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = OasisGreen
                    )
                    Text(
                        text = post.timestamp,
                        fontSize = 10.sp,
                        color = MutedText
                    )
                }
            }
        }
    }
}
