package com.werey.budgettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.PieChartOutline
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.TrendingDown
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.SimCard
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.werey.budgettracker.ui.theme.WereyBudgetTrackerTheme

private val AppBackground = Color(0xFF100D12)
private val AppSurface = Color(0xFF1B171E)
private val AppSurfaceRaised = Color(0xFF211C25)
private val AppOutline = Color(0xFF51485A)
private val Lavender = Color(0xFFD0A9FB)
private val DeepPlum = Color(0xFF3B2A52)
private val PrimaryText = Color(0xFFF4EEF6)
private val SecondaryText = Color(0xFFC9BFCC)
private val IncomeGreen = Color(0xFFB9EF9B)

private enum class Page { Home, Add }
private enum class Kind { Food, Transport, Data, Income }

private data class Transaction(
    val title: String,
    val category: String,
    val value: String,
    val kind: Kind,
    val income: Boolean = false,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { WereyBudgetTrackerTheme { BudgetApp() } }
    }
}

@Composable
private fun BudgetApp() {
    var page by rememberSaveable { mutableStateOf(Page.Home) }
    var selectedDestination by rememberSaveable { mutableIntStateOf(0) }
    var requestedKind by remember { mutableStateOf<Kind?>(null) }
    var transactions by remember {
        mutableStateOf(
            listOf(
                Transaction("Chicken & rice", "Food", "−₦3,200", Kind.Food),
                Transaction("Bus to campus", "Transport", "−₦700", Kind.Transport),
                Transaction("Salary", "Income", "+₦85,000", Kind.Income, income = true),
            )
        )
    }
    val spatial = spring<IntOffset>(dampingRatio = .76f, stiffness = 360f)
    val expressiveScale = spring<Float>(dampingRatio = .72f, stiffness = 320f)
    val effects = tween<Float>(durationMillis = 160, easing = FastOutSlowInEasing)

    BackHandler(enabled = page == Page.Add) { page = Page.Home }

    Surface(modifier = Modifier.fillMaxSize(), color = AppBackground) {
        AnimatedContent(
            targetState = page,
            modifier = Modifier.fillMaxSize().background(AppBackground),
            transitionSpec = {
                if (targetState == Page.Add) {
                    (
                        scaleIn(
                            initialScale = .06f,
                            transformOrigin = TransformOrigin(.92f, .96f),
                            animationSpec = expressiveScale,
                        ) +
                            slideInVertically(animationSpec = spatial) { it / 4 } +
                            fadeIn(animationSpec = effects)
                        ) togetherWith fadeOut(animationSpec = tween(70))
                } else {
                    (fadeIn(animationSpec = effects) + scaleIn(initialScale = .96f, animationSpec = expressiveScale)) togetherWith
                        (
                            scaleOut(
                                targetScale = .06f,
                                transformOrigin = TransformOrigin(.92f, .96f),
                                animationSpec = expressiveScale,
                            ) +
                                slideOutVertically(animationSpec = spatial) { it / 4 } +
                                fadeOut(animationSpec = tween(100))
                            )
                }.using(SizeTransform(clip = false))
            },
            label = "screen transition",
        ) { target ->
            when (target) {
                Page.Home -> HomeScreen(
                    transactions = transactions,
                    selectedDestination = selectedDestination,
                    onDestinationSelected = { selectedDestination = it },
                    onAdd = { kind ->
                        requestedKind = kind
                        page = Page.Add
                    },
                )
                Page.Add -> AddTransactionScreen(
                    initialKind = requestedKind,
                    onBack = { page = Page.Home },
                    onSave = {
                        transactions = listOf(it) + transactions
                        page = Page.Home
                    },
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    transactions: List<Transaction>,
    selectedDestination: Int,
    onDestinationSelected: (Int) -> Unit,
    onAdd: (Kind?) -> Unit,
) {
    Scaffold(
        containerColor = AppBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            HomeBottomBar(
                selectedDestination = selectedDestination,
                onDestinationSelected = onDestinationSelected,
                onAdd = onAdd,
            )
        },
    ) { scaffoldPadding ->
        AnimatedContent(
            targetState = selectedDestination,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                val forward = targetState > initialState
                val enterDirection = if (forward) 1 else -1
                (slideInHorizontally(
                    animationSpec = spring(dampingRatio = .82f, stiffness = 420f),
                    initialOffsetX = { it * enterDirection / 5 },
                ) + fadeIn(animationSpec = tween(160))) togetherWith
                    (slideOutHorizontally(
                        animationSpec = spring(dampingRatio = .88f, stiffness = 520f),
                        targetOffsetX = { -it * enterDirection / 7 },
                    ) + fadeOut(animationSpec = tween(110)))
            },
            label = "bottom destination",
        ) { destination ->
            when (destination) {
                1 -> PlanContent(scaffoldPadding)
                2 -> InsightsContent(scaffoldPadding)
                else -> DashboardContent(scaffoldPadding, transactions, onAdd)
            }
        }
    }
}

@Composable
private fun DashboardContent(
    scaffoldPadding: PaddingValues,
    transactions: List<Transaction>,
    onAdd: (Kind?) -> Unit,
) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 16.dp, bottom = 20.dp),
        ) {
            item {
                HomeHeader()
                Spacer(Modifier.height(22.dp))
                BalanceCard()
                Spacer(Modifier.height(22.dp))
                SectionTitle("Quick add")
                Spacer(Modifier.height(14.dp))
                QuickAddRow(onAdd)
                Spacer(Modifier.height(28.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SectionTitle("Today")
                    Text(
                        text = "See all",
                        color = Lavender,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Spacer(Modifier.height(12.dp))
            }
            items(transactions, key = { it.title + it.value }) { transaction ->
                TransactionRow(transaction)
                Spacer(Modifier.height(8.dp))
            }
        }
}

@Composable
private fun HomeHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = "Good morning, Kim",
                color = PrimaryText,
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = "Your money, clearly planned.",
                color = SecondaryText,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = Lavender,
            contentColor = DeepPlum,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("K", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
private fun BalanceCard() {
    var progressTarget by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) { progressTarget = .63f }
    val progress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = spring(dampingRatio = .78f, stiffness = 180f),
        label = "available budget progress",
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Lavender, contentColor = DeepPlum),
    ) {
        Column(Modifier.padding(horizontal = 22.dp, vertical = 20.dp)) {
            Text("AVAILABLE TO SPEND", style = MaterialTheme.typography.labelSmall)
            Spacer(Modifier.height(8.dp))
            Text("₦42,550", style = MaterialTheme.typography.displaySmall)
            Spacer(Modifier.height(2.dp))
            Text("until 30 October", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape)
                    .background(DeepPlum)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(IncomeGreen)
                )
            }
        }
    }
}

@Composable
private fun QuickAddRow(onAdd: (Kind?) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        QuickAddCard("Food", "₦1,200", Kind.Food, Modifier.weight(1f), onAdd)
        QuickAddCard("Transport", "₦500", Kind.Transport, Modifier.weight(1f), onAdd)
        QuickAddCard("Data", "₦2,000", Kind.Data, Modifier.weight(1f), onAdd)
    }
}

@Composable
private fun QuickAddCard(
    title: String,
    amount: String,
    kind: Kind,
    modifier: Modifier,
    onAdd: (Kind?) -> Unit,
) {
    Card(
        onClick = { onAdd(kind) },
        modifier = modifier.height(82.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(kind.icon(), contentDescription = null, tint = Lavender, modifier = Modifier.size(20.dp))
            Column {
                Text(title, color = PrimaryText, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, maxLines = 1)
                Text(amount, color = SecondaryText, style = MaterialTheme.typography.labelSmall, maxLines = 1)
            }
        }
    }
}

@Composable
private fun TransactionRow(transaction: Transaction) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = AppSurfaceRaised,
        border = BorderStroke(1.dp, AppOutline),
    ) {
        Row(
            modifier = Modifier.height(64.dp).padding(horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = DeepPlum,
                contentColor = Lavender,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(transaction.kind.icon(), contentDescription = null, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(transaction.title, color = PrimaryText, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(transaction.category, color = SecondaryText, style = MaterialTheme.typography.labelSmall)
            }
            Text(
                transaction.value,
                color = if (transaction.income) IncomeGreen else PrimaryText,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun PlanContent(scaffoldPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(scaffoldPadding).statusBarsPadding(),
        contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 16.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            DestinationHeader("Plan", "October 2026")
            Spacer(Modifier.height(22.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = Lavender, contentColor = DeepPlum),
            ) {
                Column(Modifier.padding(22.dp)) {
                    Text("MONTHLY BUDGET", style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.height(8.dp))
                    Text("₦120,000", style = MaterialTheme.typography.displaySmall)
                    Spacer(Modifier.height(4.dp))
                    Text("₦42,550 still available", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(Modifier.height(24.dp))
            SectionTitle("Plan your money")
            Spacer(Modifier.height(12.dp))
        }
        item { PlanMenuCard(Icons.Outlined.PieChartOutline, "Budgets", "4 category limits", "₦77,450 used") }
        item { PlanMenuCard(Icons.Outlined.Savings, "Savings goals", "Laptop fund", "68%") }
        item { PlanMenuCard(Icons.Outlined.Autorenew, "Recurring payments", "3 payments this month", "₦12,800") }
        item {
            Spacer(Modifier.height(12.dp))
            SectionTitle("Category limits")
            Spacer(Modifier.height(12.dp))
            BudgetProgressRow("Food", "₦18,000 of ₦25,000", .72f, Icons.Outlined.Restaurant)
            Spacer(Modifier.height(10.dp))
            BudgetProgressRow("Transport", "₦9,300 of ₦15,000", .62f, Icons.Outlined.DirectionsBus)
            Spacer(Modifier.height(10.dp))
            BudgetProgressRow("Data", "₦6,000 of ₦10,000", .60f, Icons.Outlined.SimCard)
        }
    }
}

@Composable
private fun InsightsContent(scaffoldPadding: PaddingValues) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(scaffoldPadding).statusBarsPadding(),
        contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 16.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            DestinationHeader("Insights", "This month")
            Spacer(Modifier.height(22.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = AppSurfaceRaised),
                border = BorderStroke(1.dp, AppOutline),
            ) {
                Column(Modifier.padding(22.dp)) {
                    Text("TOTAL SPENT", color = SecondaryText, style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.height(8.dp))
                    Text("₦77,450", color = PrimaryText, style = MaterialTheme.typography.displaySmall)
                    Spacer(Modifier.height(5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.AutoMirrored.Outlined.TrendingDown, contentDescription = null, tint = IncomeGreen, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("12% less than September", color = IncomeGreen, style = MaterialTheme.typography.labelMedium)
                    }
                    Spacer(Modifier.height(22.dp))
                    SpendingBars()
                }
            }
            Spacer(Modifier.height(24.dp))
            SectionTitle("Where it went")
            Spacer(Modifier.height(12.dp))
            InsightCategory("Food", "₦31,200", "40%", .40f, Icons.Outlined.Restaurant)
            Spacer(Modifier.height(10.dp))
            InsightCategory("Transport", "₦18,700", "24%", .24f, Icons.Outlined.DirectionsBus)
            Spacer(Modifier.height(10.dp))
            InsightCategory("Data", "₦12,000", "15%", .15f, Icons.Outlined.SimCard)
            Spacer(Modifier.height(24.dp))
            SectionTitle("A useful nudge")
            Spacer(Modifier.height(12.dp))
            Surface(shape = RoundedCornerShape(22.dp), color = DeepPlum, contentColor = Lavender) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Insights, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    Text("Transport spending is on track to finish ₦3,100 under budget.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun DestinationHeader(title: String, period: String) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Column {
            Text(title, color = PrimaryText, style = MaterialTheme.typography.headlineSmall)
            Text(period, color = SecondaryText, style = MaterialTheme.typography.bodyMedium)
        }
        Surface(Modifier.size(48.dp), CircleShape, color = Lavender, contentColor = DeepPlum) {
            Box(contentAlignment = Alignment.Center) { Text("K", style = MaterialTheme.typography.titleMedium) }
        }
    }
}

@Composable
private fun PlanMenuCard(icon: ImageVector, title: String, subtitle: String, trailing: String) {
    Card(
        onClick = {},
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = AppSurface),
    ) {
        Row(Modifier.height(72.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(Modifier.size(42.dp), CircleShape, color = DeepPlum, contentColor = Lavender) {
                Box(contentAlignment = Alignment.Center) { Icon(icon, contentDescription = null, modifier = Modifier.size(21.dp)) }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = PrimaryText, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(subtitle, color = SecondaryText, style = MaterialTheme.typography.labelSmall)
            }
            Text(trailing, color = Lavender, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun BudgetProgressRow(label: String, detail: String, progress: Float, icon: ImageVector) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = AppSurface) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Lavender, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text(label, color = PrimaryText, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(((progress * 100).toInt()).toString() + "%", color = Lavender, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(Modifier.height(5.dp))
                Box(Modifier.fillMaxWidth().height(6.dp).clip(CircleShape).background(DeepPlum)) {
                    Box(Modifier.fillMaxWidth(progress).fillMaxHeight().clip(CircleShape).background(Lavender))
                }
                Spacer(Modifier.height(5.dp))
                Text(detail, color = SecondaryText, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun SpendingBars() {
    val heights = listOf(32, 48, 38, 68, 53, 78, 61)
    Row(
        modifier = Modifier.fillMaxWidth().height(92.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        heights.forEachIndexed { index, height ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.width(22.dp).height(height.dp).clip(RoundedCornerShape(8.dp)).background(if (index == 5) Lavender else DeepPlum))
                Spacer(Modifier.height(5.dp))
                Text(listOf("M", "T", "W", "T", "F", "S", "S")[index], color = SecondaryText, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun InsightCategory(label: String, amount: String, percent: String, progress: Float, icon: ImageVector) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), color = AppSurface) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(Modifier.size(40.dp), CircleShape, color = DeepPlum, contentColor = Lavender) {
                Box(contentAlignment = Alignment.Center) { Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp)) }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text(label, color = PrimaryText, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text(amount, color = PrimaryText, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(7.dp))
                Box(Modifier.fillMaxWidth().height(5.dp).clip(CircleShape).background(DeepPlum)) {
                    Box(Modifier.fillMaxWidth(progress).fillMaxHeight().clip(CircleShape).background(Lavender))
                }
            }
            Spacer(Modifier.width(10.dp))
            Text(percent, color = Lavender, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun HomeBottomBar(
    selectedDestination: Int,
    onDestinationSelected: (Int) -> Unit,
    onAdd: (Kind?) -> Unit,
) {
    val destinations = listOf(
        Triple("Home", Icons.Outlined.Home, Icons.Rounded.Home),
        Triple("Plan", Icons.Outlined.CalendarMonth, Icons.Rounded.CalendarMonth),
        Triple("Insights", Icons.Outlined.Insights, Icons.Rounded.Insights),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground)
            .navigationBarsPadding()
            .padding(horizontal = 22.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier
                .weight(1f)
                .height(64.dp),
            shape = RoundedCornerShape(32.dp),
            color = AppSurface,
            contentColor = SecondaryText,
            border = BorderStroke(1.dp, AppOutline),
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                destinations.forEachIndexed { index, (label, outlineIcon, selectedIcon) ->
                    val selected = selectedDestination == index
                    Surface(
                        selected = selected,
                        onClick = { onDestinationSelected(index) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        shape = RoundedCornerShape(26.dp),
                        color = if (selected) DeepPlum else Color.Transparent,
                        contentColor = if (selected) Lavender else SecondaryText,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                        Icon(
                            imageVector = if (selected) selectedIcon else outlineIcon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                            Text(
                                label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            )
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { onAdd(null) },
            modifier = Modifier.size(58.dp),
            shape = CircleShape,
            containerColor = Lavender,
            contentColor = DeepPlum,
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Add transaction", modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
private fun AddTransactionScreen(
    initialKind: Kind?,
    onBack: () -> Unit,
    onSave: (Transaction) -> Unit,
) {
    var incomeMode by rememberSaveable { mutableStateOf(initialKind == Kind.Income) }
    var amount by rememberSaveable { mutableStateOf(amountFor(initialKind)) }
    val title = if (incomeMode) "Salary" else when (initialKind) {
        Kind.Transport -> "Bus to campus"
        Kind.Data -> "Mobile data"
        else -> "Lunch and drink"
    }
    val category = if (incomeMode) "Income" else if (initialKind == Kind.Transport) "Transport" else "Food"

    Scaffold(
        containerColor = AppBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(color = AppBackground) {
                Button(
                    onClick = {
                        onSave(
                            Transaction(
                                title = title,
                                category = category,
                                value = (if (incomeMode) "+₦" else "−₦") + amount,
                                kind = if (incomeMode) Kind.Income else initialKind ?: Kind.Food,
                                income = incomeMode,
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 22.dp, vertical = 12.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Lavender, contentColor = DeepPlum),
                ) {
                    Text(
                        if (incomeMode) "Save income" else "Save expense",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        },
    ) { scaffoldPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 14.dp, bottom = 16.dp),
        ) {
            item {
                AddHeader(onBack)
                Spacer(Modifier.height(16.dp))
                TypeSelector(incomeMode = incomeMode, onChange = { incomeMode = it })
                Spacer(Modifier.height(22.dp))
                AmountDisplay(amount)
                Spacer(Modifier.height(18.dp))
                TransactionDetail(title = title, incomeMode = incomeMode)
                Spacer(Modifier.height(18.dp))
                Text("Pinned", color = SecondaryText, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                PinnedAmounts { amount = formatAmount(it.filter(Char::isDigit)) }
                Spacer(Modifier.height(15.dp))
                Keypad { amount = changeAmount(amount, it) }
            }
        }
    }
}

@Composable
private fun AddHeader(onBack: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        FilledIconButton(
            onClick = onBack,
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = AppSurface,
                contentColor = PrimaryText,
            ),
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
        }
        Text(
            text = "Add transaction",
            modifier = Modifier.weight(1f),
            color = PrimaryText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.size(48.dp))
    }
}

@Composable
private fun TypeSelector(incomeMode: Boolean, onChange: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = AppSurface,
    ) {
        Row(Modifier.height(48.dp).padding(4.dp)) {
            TypeOption("Expense", selected = !incomeMode, Modifier.weight(1f)) { onChange(false) }
            TypeOption("Income", selected = incomeMode, Modifier.weight(1f)) { onChange(true) }
        }
    }
}

@Composable
private fun TypeOption(label: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val background by animateColorAsState(
        targetValue = if (selected) Lavender else Color.Transparent,
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "$label background",
    )
    val foreground by animateColorAsState(
        targetValue = if (selected) DeepPlum else SecondaryText,
        animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing),
        label = "$label foreground",
    )
    val corner by animateDpAsState(
        targetValue = if (selected) 24.dp else 12.dp,
        animationSpec = spring(dampingRatio = .72f, stiffness = 420f),
        label = "$label shape",
    )
    Surface(
        selected = selected,
        onClick = onClick,
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(corner),
        color = background,
        contentColor = foreground,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun AmountDisplay(amount: String) {
    var previousAmount by remember { mutableStateOf(amount) }
    val previousDigits = previousAmount.count(Char::isDigit)
    val currentDigits = amount.count(Char::isDigit)
    val animateNewDigit = currentDigits > previousDigits
    SideEffect { previousAmount = amount }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text("AMOUNT", color = SecondaryText, style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(7.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "₦" + amount.dropLast(1),
                color = PrimaryText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
            )
            ExpressiveDigitReveal(
                digit = amount.lastOrNull() ?: '0',
                eventKey = amount,
                animate = animateNewDigit,
            )
        }
        Spacer(Modifier.height(3.dp))
        Text("Use the keypad to edit", color = SecondaryText, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ExpressiveDigitReveal(digit: Char, eventKey: String, animate: Boolean) {
    var showDigit by remember(eventKey) { mutableStateOf(!animate) }
    LaunchedEffect(eventKey) {
        if (animate) {
            delay(115)
            showDigit = true
        }
    }
    val shapeScale by animateFloatAsState(
        targetValue = if (showDigit) 0f else 1f,
        animationSpec = spring(dampingRatio = .7f, stiffness = 560f),
        label = "typed digit shape",
    )
    val digitScale by animateFloatAsState(
        targetValue = if (showDigit) 1f else .55f,
        animationSpec = spring(dampingRatio = .62f, stiffness = 520f),
        label = "typed digit scale",
    )
    val digitAlpha by animateFloatAsState(
        targetValue = if (showDigit) 1f else 0f,
        animationSpec = tween(durationMillis = 150, delayMillis = if (animate) 45 else 0),
        label = "typed digit alpha",
    )
    val shape = when (digit.digitToIntOrNull()?.rem(3)) {
        0 -> RoundedCornerShape(topStart = 5.dp, topEnd = 18.dp, bottomEnd = 7.dp, bottomStart = 15.dp)
        1 -> RoundedCornerShape(topStart = 17.dp, topEnd = 6.dp, bottomEnd = 16.dp, bottomStart = 5.dp)
        else -> RoundedCornerShape(topStart = 7.dp, topEnd = 15.dp, bottomEnd = 5.dp, bottomStart = 18.dp)
    }

    Box(
        modifier = Modifier.width(29.dp).height(48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .size(31.dp)
                .graphicsLayer {
                    scaleX = shapeScale
                    scaleY = shapeScale
                    rotationZ = (1f - shapeScale) * 18f
                    alpha = shapeScale
                }
                .clip(shape)
                .background(Lavender)
        )
        Text(
            text = digit.toString(),
            color = PrimaryText,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.graphicsLayer {
                scaleX = digitScale
                scaleY = digitScale
                alpha = digitAlpha
            },
        )
    }
}

@Composable
private fun TransactionDetail(title: String, incomeMode: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = AppSurfaceRaised,
        border = BorderStroke(1.dp, AppOutline),
    ) {
        Row(
            modifier = Modifier.height(60.dp).padding(horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = CircleShape,
                color = DeepPlum,
                contentColor = Lavender,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        if (incomeMode) Icons.Rounded.Payments else Icons.AutoMirrored.Outlined.ReceiptLong,
                        contentDescription = null,
                        modifier = Modifier.size(19.dp),
                    )
                }
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("What was this for?", color = SecondaryText, style = MaterialTheme.typography.labelSmall)
                Text(title, color = PrimaryText, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PinnedAmounts(onPick: (String) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf("Fanta ₦500", "Lunch ₦2,500", "Bus ₦700").forEach { label ->
            Surface(
                onClick = { onPick(label) },
                modifier = Modifier.weight(1f).height(40.dp),
                shape = RoundedCornerShape(20.dp),
                color = DeepPlum,
                contentColor = Lavender,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, maxLines = 1)
                }
            }
        }
    }
}

@Composable
private fun Keypad(onKey: (String) -> Unit) {
    val keys = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "0", "delete")
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        keys.chunked(3).forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { key ->
                    KeypadButton(key = key, modifier = Modifier.weight(1f)) { onKey(key) }
                }
            }
        }
    }
}

@Composable
private fun KeypadButton(key: String, modifier: Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) .96f else 1f,
        animationSpec = if (pressed) tween(90) else spring(dampingRatio = .7f, stiffness = 380f),
        label = "key press",
    )
    val corner by animateDpAsState(
        targetValue = if (pressed) 16.dp else 28.dp,
        animationSpec = if (pressed) tween(90) else tween(280, easing = FastOutSlowInEasing),
        label = "key shape",
    )
    val containerColor by animateColorAsState(
        targetValue = if (pressed) Lavender else AppSurface,
        animationSpec = if (pressed) tween(90) else tween(280),
        label = "key color",
    )
    val contentColor by animateColorAsState(
        targetValue = if (pressed) DeepPlum else PrimaryText,
        animationSpec = if (pressed) tween(90) else tween(280),
        label = "key content color",
    )
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale },
        interactionSource = interactionSource,
        shape = RoundedCornerShape(corner),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
    ) {
        if (key == "delete") {
            Icon(Icons.Outlined.DeleteOutline, contentDescription = "Delete last digit")
        } else {
            Text(key, style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, color = PrimaryText, style = MaterialTheme.typography.titleLarge)
}

private fun Kind.icon(): ImageVector = when (this) {
    Kind.Food -> Icons.Outlined.Restaurant
    Kind.Transport -> Icons.Outlined.DirectionsBus
    Kind.Data -> Icons.Outlined.SimCard
    Kind.Income -> Icons.Rounded.Payments
}

private fun amountFor(kind: Kind?): String = when (kind) {
    Kind.Food -> "1,200"
    Kind.Transport -> "500"
    Kind.Data -> "2,000"
    Kind.Income -> "85,000"
    null -> "2,800"
}

private fun formatAmount(raw: String): String =
    raw.reversed().chunked(3).joinToString(",").reversed().ifBlank { "0" }

private fun changeAmount(current: String, key: String): String {
    val raw = current.replace(",", "")
    val next = when (key) {
        "delete" -> raw.dropLast(1)
        "." -> if (raw.contains(".")) raw else "$raw."
        else -> if (raw == "0") key else raw + key
    }
    val parts = next.split(".")
    return if (parts.size > 1) formatAmount(parts[0]) + "." + parts[1] else formatAmount(next)
}

@Preview(device = "spec:width=393dp,height=852dp,dpi=440")
@Composable
private fun BudgetAppPreview() {
    WereyBudgetTrackerTheme { BudgetApp() }
}
